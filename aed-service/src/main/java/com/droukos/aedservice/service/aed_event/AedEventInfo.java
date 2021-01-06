package com.droukos.aedservice.service.aed_event;

import com.droukos.aedservice.config.redis.RedisConfigProperties;
import com.droukos.aedservice.environment.constants.authorities.Roles;
import com.droukos.aedservice.environment.dto.RequesterAccessTokenData;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoClose;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoRescuerSub;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoSearch;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.EventPreviewUsersDto;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.RequestedAedEvent;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.UnionEventDeviceRescuerDto;
import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceRescuer;
import com.droukos.aedservice.environment.dto.server.user.RequestedPreviewUser;
import com.droukos.aedservice.environment.enums.AedEventStatus;
import com.droukos.aedservice.environment.enums.DeviceAvailability;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.model.user.UserRes;
import com.droukos.aedservice.repo.AedDeviceRepository;
import com.droukos.aedservice.repo.AedEventRepository;
import com.droukos.aedservice.repo.UserRepository;
import com.droukos.aedservice.service.validator.aed_event.OccurrenceTypeValidator;
import com.droukos.aedservice.util.SecurityUtil;
import com.droukos.aedservice.util.ValidatorUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import static com.droukos.aedservice.environment.constants.AedStatusCodes.SDISABLED;
import static com.droukos.aedservice.environment.constants.AedTypeCodes.TDISABLED;
import static com.droukos.aedservice.environment.constants.authorities.Roles.GENERAL_ADMIN;
import static com.droukos.aedservice.environment.enums.AedEventStatus.PENDING;
import static com.droukos.aedservice.util.factories.HttpExceptionFactory.badRequest;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@AllArgsConstructor
public class AedEventInfo {
    private final RedisConfigProperties redisConfigProperties;
    private final AedEventRepository aedEventRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final AedDeviceRepository aedDeviceRepository;
    private final UserRepository userRepository;


    public void validateType(AedEventDtoSearch aedEventDtoSearch) {
        ValidatorUtil.validate(aedEventDtoSearch, new OccurrenceTypeValidator());
    }

    public Flux<AedEvent> findUnassignedEvents() {
        return aedEventRepository.findAedEventsByStatus(PENDING.getStatus());
    }

    public Mono<AedEventDtoRescuerSub> fetchRescuerFromDtoOrContext(Tuple2<SecurityContext, AedEventDtoRescuerSub> tuple2) {
        SecurityContext context = tuple2.getT1();
        AedEventDtoRescuerSub dto = tuple2.getT2();

        return Mono.just(
                SecurityUtil.getRequesterRoles(context).stream().noneMatch(role -> role.equals(GENERAL_ADMIN))
                        ? new AedEventDtoRescuerSub(
                        dto.getId(),
                        SecurityUtil.getRequesterUsername(context),
                        dto.getAedDeviceId(),
                        dto.getEstimatedFinish())
                        : dto
        );
    }

    public Flux<AedEvent> findEventOnFilter(AedEventDtoSearch aedEventDtoSearch) {
        if (aedEventDtoSearch.getOccurrenceType() == TDISABLED && aedEventDtoSearch.getStatus() != SDISABLED) {
            return aedEventRepository.findAedEventsByStatus(aedEventDtoSearch.getStatus());
        } else if (aedEventDtoSearch.getOccurrenceType() != TDISABLED && aedEventDtoSearch.getStatus() == SDISABLED) {
            return aedEventRepository.findAedEventsByOccurrenceType(aedEventDtoSearch.getOccurrenceType());
        } else {
            return aedEventRepository.findAedEventsByOccurrenceTypeAndStatus(aedEventDtoSearch.getOccurrenceType(), aedEventDtoSearch.getStatus());
        }
    }

    public Mono<Tuple2<AedEvent, AedEventDtoRescuerSub>> findEventByIdForRescuerSub(AedEventDtoRescuerSub dto) {
        return this.findEventById(dto.getId())
                .zipWith(Mono.just(dto));
    }

    public Mono<Tuple3<UnionEventDeviceRescuerDto, AedEventDtoRescuerSub, String>> findEventDeviceRescuerAggregation(AedEventDtoRescuerSub dto) {
        String aedEventChannel = redisConfigProperties.getAedEventSingleChannelPrefix() + dto.getId();
        return aedEventRepository.getUnionEventDeviceRescuer(dto.getId(), dto.getAedDeviceId(), dto.getRescuer())
                .flatMap(union -> Mono.zip(Mono.just(union), Mono.just(dto), Mono.just(aedEventChannel)));
    }

    public Mono<Tuple3<AedEvent, AedDevice, AedEventDtoRescuerSub>> findDeviceByIdForRescuing(Tuple2<AedEvent, AedEventDtoRescuerSub> tuple2) {
        AedEvent aedEvent = tuple2.getT1();
        return this.aedDeviceRepository.getAedDevicesByIdAndHomePNear(
                tuple2.getT2().getAedDeviceId(),
                new GeoJsonPoint(aedEvent.getOccurrencePoint().getX(), aedEvent.getOccurrencePoint().getY()),
                new Distance(4, Metrics.KILOMETERS))
                .flatMap(aedDevice -> aedDevice.getId() == null
                        ? Mono.error(badRequest("Device not found, or exceeds border boundaries"))
                        : Mono.zip(Mono.just(tuple2.getT1()), Mono.just(aedDevice), Mono.just(tuple2.getT2())));
    }

    public Mono<Tuple3<UnionEventDeviceRescuerDto, AedEventDtoRescuerSub, String>> validateAedDeviceForRescuing(Tuple3<UnionEventDeviceRescuerDto, AedEventDtoRescuerSub, String> tuple3) {
        AedDevice aedDevice = tuple3.getT1().getAedDevice();
        return aedDevice.getStatus() == DeviceAvailability.AVAILABLE.getCode() || aedDevice.getStatus() == DeviceAvailability.RETURNING.getCode()
                ? Mono.just(tuple3)
                : Mono.error(badRequest("Device status not available"));
    }

    public Mono<AedEvent> findEventById(String id) {
        return aedEventRepository.findById(id)
                .defaultIfEmpty(new AedEvent())
                .flatMap(aedEvent -> aedEvent.getId() == null
                        ? Mono.error(badRequest("Event not found"))
                        : Mono.just(aedEvent));
    }

    public Mono<AedEvent> validateIfEventIsClosable(Tuple2<AedEvent, SecurityContext> tuple2) {
        RequesterAccessTokenData requesterData = SecurityUtil.getRequesterData(tuple2.getT2());
        AedEvent aedEvent = tuple2.getT1();
        return aedEvent.getStatus() == AedEventStatus.COMPLETED.getStatus()
                ||
                (aedEvent.getRescuer() == null ||
                        (!aedEvent.getRescuer().equals(requesterData.getUsername()) && requesterData.getRoles().stream()
                                .noneMatch(role -> role.equals(Roles.RESCUER) || role.equals(GENERAL_ADMIN))))
                ? Mono.error(badRequest())
                : Mono.just(aedEvent);
    }

    public Mono<Tuple2<AedEvent, AedDevice>> saveAedEvent(Tuple2<AedEvent, AedDevice> tuple2) {
        return aedEventRepository.save(tuple2.getT1())
                .flatMap(aedEvent -> Mono.zip(Mono.just(aedEvent), Mono.just(tuple2.getT2())));
    }

    public Mono<Tuple3<AedEvent, AedDevice, UserRes>> saveUser(Tuple3<AedEvent, AedDevice, UserRes> tuple3) {
        return userRepository.save(tuple3.getT3())
                .then(Mono.just(tuple3));
    }

    public Mono<Tuple2<AedEvent, AedDevice>> saveAedDevice(Tuple2<AedEvent, AedDevice> tuple2) {
        return aedDeviceRepository.save(tuple2.getT2())
                .then(Mono.just(tuple2));
    }

    public Mono<AedEvent> saveAedEvent(AedEvent aedEvent) {
        return aedEventRepository.save(aedEvent);
    }

    public Mono<Tuple3<AedEvent, AedDevice, AedEventDtoClose>> fetchAedDeviceForClosingEvent(Tuple2<AedEvent, AedEventDtoClose> tuple2) {
        return aedDeviceRepository.getAedDeviceById(tuple2.getT1().getAedDeviceId())
                .flatMap(aedDevice -> Mono.zip(Mono.just(tuple2.getT1()), Mono.just(aedDevice), Mono.just(tuple2.getT2())));
    }

    public Mono<RequestedAedEvent> fetchEventByType(AedEvent aedEvent) {
        return Mono.just(RequestedAedEvent.build(aedEvent));
    }

    public Mono<EventPreviewUsersDto> fetchEventUsers(String id) {
        return aedEventRepository.getEventPreviewUsers(id);
    }

    public Mono<EventPreviewUsersDto> validateAndServeEventUsers(Tuple2<EventPreviewUsersDto, SecurityContext> tuple2) {

        RequesterAccessTokenData requesterData = SecurityUtil.getRequesterData(tuple2.getT2());
        EventPreviewUsersDto dto = tuple2.getT1();
        return requesterData.getUsername().equals(dto.getUsername())
                || requesterData.getRoles().stream()
                .anyMatch(role -> role.equals(Roles.GENERAL_ADMIN) || role.equals(Roles.RESCUER))
                ? Mono.just(tuple2.getT1())
                : Mono.error(badRequest());
    }

    public Flux<AedDeviceRescuer> fetchDeviceRescuer(String aedDeviceId) {
        return reactiveMongoTemplate.aggregate(
                newAggregation(
                        match(Criteria.where("_id").is(aedDeviceId)),
                        lookup("userRes", "onUId", "user", "rescuer"),
                        unwind("rescuer"),
                        group("uniqNickname")
                                .first("uniqNickname").as("uniqueNickname")
                                .first("modelName").as("modelName")
                                .first("desc").as("description")
                                .first("status").as("status")
                                .first("picUrl").as("picUrl")
                                .first("addr").as("address")
                                .first("homeP").as("homePoint")
                                .first("onEvId").as("onEventId")
                                .first("takenOn").as("takenOn")
                                .first("onEstFin").as("estimatedFinish")
                                .first("rescuer.user").as("rescuerUsername")
                                .first("rescuer.email").as("rescuerEmail")
                                .first("rescuer.prsn.phones.rescuer.phone").as("rescuerPhone")
                                .first("rescuer.prsn.name").as("rescuerName")
                                .first("rescuer.prsn.sur").as("rescuerSurname")
                                .first("rescuer.prsn.prof.av").as("rescuerAvatar")
                                .first("rescuer.appState.status").as("rescuerStatus")
                                .first("rescuer.allRoles.role").as("rescuerRoles")
                ), AedDevice.class, AedDeviceRescuer.class);
    }
}
