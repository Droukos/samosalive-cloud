package com.droukos.aedservice.service.aed_event;

import com.droukos.aedservice.config.redis.RedisConfigProperties;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoClose;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoRescuerSub;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoSearch;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.RequestedAedEvent;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.RequestedPreviewAedEvent;
import com.droukos.aedservice.environment.enums.AedEventStatus;
import com.droukos.aedservice.environment.enums.DeviceAvailability;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.repo.AedDeviceRepository;
import com.droukos.aedservice.repo.AedEventRepository;
import com.droukos.aedservice.service.validator.aed_event.OccurrenceTypeValidator;
import com.droukos.aedservice.util.SecurityUtil;
import com.droukos.aedservice.util.ValidatorUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
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

@Service
@AllArgsConstructor
public class AedEventInfo {
    private final AedEventRepository aedEventRepository;
    private final AedDeviceRepository aedDeviceRepository;


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
                SecurityUtil.getRequesterRoles(context).stream().noneMatch(role-> role.equals(GENERAL_ADMIN))
                ? new AedEventDtoRescuerSub(dto.getId(), SecurityUtil.getRequesterUsername(context), dto.getAedDeviceId())
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
       return this.findEventId(dto.getId())
               .zipWith(Mono.just(dto));
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

    public Mono<Tuple3<AedEvent, AedDevice, AedEventDtoRescuerSub>> validateAedDeviceForRescuing(Tuple3<AedEvent, AedDevice, AedEventDtoRescuerSub> tuple3) {
        AedDevice aedDevice = tuple3.getT2();
        return aedDevice.getStatus() == DeviceAvailability.AVAILABLE.getCode() || aedDevice.getStatus()== DeviceAvailability.RETURNING.getCode()
                ? Mono.just(tuple3)
                : Mono.error(badRequest("Device status not available"));
    }

    public Mono<AedEvent> findEventId(String id) {
        return aedEventRepository.findById(id)
                .defaultIfEmpty(new AedEvent())
                .flatMap(aedEvent -> aedEvent.getId() == null ? Mono.error(badRequest("Event not found")) : Mono.just(aedEvent));
    }

    public Mono<Tuple2<AedEvent, AedDevice>> saveAedEvent(Tuple2<AedEvent, AedDevice> tuple2) {
        return aedEventRepository.save(tuple2.getT1())
                .flatMap(aedEvent -> Mono.zip(Mono.just(aedEvent), Mono.just(tuple2.getT2())));
    }

    public Mono<AedEvent> saveAedDevice(Tuple2<AedEvent, AedDevice> tuple2) {
        return aedDeviceRepository.save(tuple2.getT2())
                .then(Mono.just(tuple2.getT1()));
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
}
