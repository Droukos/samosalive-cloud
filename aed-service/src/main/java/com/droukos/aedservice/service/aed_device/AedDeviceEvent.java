package com.droukos.aedservice.service.aed_device;

import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceForEventDto;
import com.droukos.aedservice.environment.enums.DeviceAvailability;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.repo.AedDeviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import static com.droukos.aedservice.environment.enums.DeviceAvailability.*;
import static com.droukos.aedservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@AllArgsConstructor
public class AedDeviceEvent {

    private final AedDeviceRepository aedDeviceRepository;

    public Mono<AedDevice> fetchAedDeviceById(AedDeviceForEventDto dto) {
        return aedDeviceRepository.getAedDeviceById(dto.getAedDeviceId())
                .defaultIfEmpty(new AedDevice())
                .flatMap(aedDevice -> aedDevice.getId() != null
                        ? Mono.just(aedDevice)
                        : Mono.error(badRequest("Device not found")));
    }

    public Mono<Tuple2<AedDeviceForEventDto, AedDevice>> validateAedDeviceStatus(Tuple2<AedDeviceForEventDto, AedDevice> tuple2) {
        int deviceStatus = tuple2.getT2().getStatus();
        return (deviceStatus != BROKEN.getCode() && deviceStatus != STOLEN.getCode() && deviceStatus != ON_REPAIR.getCode())
                ? Mono.just(tuple2)
                : Mono.error(badRequest("Device isn't available"));
    }

    public Mono<AedDevice> saveAedDeviceOnEvent(AedDevice aedDevice) {
        return aedDeviceRepository.save(aedDevice);
    }
}
