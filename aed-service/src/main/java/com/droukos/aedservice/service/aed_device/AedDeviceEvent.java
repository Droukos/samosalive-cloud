package com.droukos.aedservice.service.aed_device;

import com.droukos.aedservice.environment.constants.authorities.Roles;
import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceForEventDto;
import com.droukos.aedservice.environment.enums.AedEventStatus;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.repo.AedDeviceRepository;
import com.droukos.aedservice.repo.AedEventRepository;
import com.droukos.aedservice.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import static com.droukos.aedservice.environment.enums.DeviceAvailability.*;
import static com.droukos.aedservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@AllArgsConstructor
public class AedDeviceEvent {

    private final AedDeviceRepository aedDeviceRepository;
    private final AedEventRepository aedEventRepository;

    public Mono<AedDevice> fetchAedDeviceById(AedDeviceForEventDto dto) {
        return aedDeviceRepository.getAedDeviceById(dto.getAedDeviceId())
                .defaultIfEmpty(new AedDevice())
                .flatMap(aedDevice -> aedDevice.getId() != null
                        ? Mono.just(aedDevice)
                        : Mono.error(badRequest("Device not found")));
    }

    public Mono<Void> validateAdminOrSameUser(Tuple2<AedDeviceForEventDto, SecurityContext> tuple2) {
        AedDeviceForEventDto dto = tuple2.getT1();
        return Mono.just(SecurityUtil.getRequesterData(tuple2.getT2()))
                .flatMap(requesterAccData -> requesterAccData.getUserId().equals(dto.getUserId()) ||
                        requesterAccData.getRoles()
                                .stream()
                                .anyMatch(role -> role.equals(Roles.GENERAL_ADMIN) || role.equals(Roles.AREA_ADMIN))
                        ? Mono.empty()
                        : Mono.error(badRequest())
                );
    }

    public Mono<AedEvent> fetchAedEventById(AedDeviceForEventDto dto) {
        return aedEventRepository.findById(dto.getEventId())
                .defaultIfEmpty(new AedEvent())
                .flatMap(aedEvent -> aedEvent.getId() != null
                        ? Mono.just(aedEvent)
                        : Mono.error(badRequest("Event not found")));
    }

    public Mono<Tuple3<AedDeviceForEventDto, AedDevice, AedEvent>> validateAedEventDeviceSub(Tuple3<AedDeviceForEventDto, AedDevice, AedEvent> tuple3) {
        int deviceStatus = tuple3.getT2().getStatus();
        AedEvent aedEvent = tuple3.getT3();
        return deviceStatus != BROKEN.getCode()
                && deviceStatus != STOLEN.getCode()
                && deviceStatus != ON_REPAIR.getCode()
                //&& aedEvent.getAedDeviceId() == null
                && aedEvent.getStatus() != AedEventStatus.COMPLETED.getStatus()
                ? Mono.just(tuple3)
                : Mono.error(badRequest("Device isn't available or event already completed or has another device"));
    }

    public Mono<Boolean> saveAedDeviceAndEvent(Tuple2<AedDevice, AedEvent> tuple2) {
        return aedDeviceRepository.save(tuple2.getT1())
                .then(aedEventRepository.save(tuple2.getT2()))
                .then(Mono.just(true));
    }
}
