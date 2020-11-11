package com.droukos.aedservice.service.aed_device;

import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceRegisterDto;
import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceCdnToken;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.repo.AedDeviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.droukos.aedservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@AllArgsConstructor
public class AedDeviceRegister {

    private final AedDeviceRepository aedDeviceRepository;
    private final ReactiveStringRedisTemplate redisTemplate;

    public Mono<AedDeviceRegisterDto> validateRegisterDto(AedDeviceRegisterDto dto) {
        return (dto.getDescription().length() <= 500
                && dto.getNickname().length() < 40
                && dto.getAddress().length() < 100
                && dto.getModelName().length() < 40)
                ? Mono.just(dto) : Mono.error(badRequest());

    }

    public Mono<AedDevice> aedDeviceToMongoDB(AedDevice aedDevice) {
        return aedDeviceRepository.save(aedDevice);
    }

    public Mono<AedDeviceCdnToken> buildAedDeviceCdnToken(AedDevice aedDevice) {
        return Mono.just(AedDeviceCdnToken.buildTokenForNewDev(aedDevice.getAddedBy()));
    }

    //public Mono<AedDeviceCdnToken> saveOnRedis(AedDeviceCdnToken aedDeviceCdnToken) {
    //    return redisTemplate.opsForValue().set(aedDeviceCdnToken.getId(), aedDeviceCdnToken.getToken(), )
    //}
}
