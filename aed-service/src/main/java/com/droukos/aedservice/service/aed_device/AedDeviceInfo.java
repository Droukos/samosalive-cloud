package com.droukos.aedservice.service.aed_device;

import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceIdDto;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.repo.AedDeviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AedDeviceInfo {

    private final AedDeviceRepository aedDeviceRepository;

    public Mono<AedDevice> fetchAedDeviceById(AedDeviceIdDto aedDeviceIdDto) {
        return aedDeviceRepository.getAedDeviceById(aedDeviceIdDto.getId());
    }
}
