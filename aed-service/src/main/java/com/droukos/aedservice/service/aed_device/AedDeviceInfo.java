package com.droukos.aedservice.service.aed_device;

import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceAreaSearchDto;
import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceEditDto;
import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceIdDto;
import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceNicknameDto;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.repo.AedDeviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.droukos.aedservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@AllArgsConstructor
public class AedDeviceInfo {

    private final AedDeviceRepository aedDeviceRepository;

    public Mono<AedDevice> fetchAedDeviceById(AedDeviceIdDto aedDeviceIdDto) {
        return aedDeviceRepository.getAedDeviceById(aedDeviceIdDto.getId());
    }
    public Mono<AedDevice> fetchAedDeviceById(String id) {
        return aedDeviceRepository.getAedDeviceById(id);
    }

    public Flux<AedDevice> fetchAedDevicesByNickname(AedDeviceNicknameDto aedDeviceNicknameDto) {
        return aedDeviceRepository.getAedDevicesByUniqNicknameLike(aedDeviceNicknameDto.getAedDeviceNickname());
    }

    public Mono<AedDeviceEditDto> validateAedDeviceEdit(AedDeviceEditDto dto) {
        return (dto.getModelDescription().length() <= 500
                && dto.getAddress().length() < 100
                && dto.getModelName().length() < 40)
                ? Mono.just(dto) : Mono.error(badRequest());
    }

    public Mono<AedDeviceAreaSearchDto> validateAedDeviceMaxDistance(AedDeviceAreaSearchDto dto) {
        return dto.getDistance() < 10
                ? Mono.just(dto)
                : Mono.error(badRequest("Over max distance was given"));
    }

    public Flux<AedDevice> findAedDeviceInArea(AedDeviceAreaSearchDto dto) {
       return aedDeviceRepository
               .getAedDevicesByHomePNear(new GeoJsonPoint(dto.getX(), dto.getY()), new Distance(dto.getDistance(), Metrics.KILOMETERS));
    }

    public Mono<AedDevice> saveAedDevice(AedDevice aedDevice) {
        return aedDeviceRepository.save(aedDevice);
    }
}
