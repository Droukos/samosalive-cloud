package com.droukos.aedservice.service.aed_device;

import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceRegisterDto;
import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceCdnToken;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.repo.AedDeviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import static com.droukos.aedservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@AllArgsConstructor
public class AedDeviceRegister {

    private final AedDeviceRepository aedDeviceRepository;

    public Mono<AedDeviceRegisterDto> validateRegisterDto(AedDeviceRegisterDto dto) {
        return (dto.getDescription().length() <= 500
                && dto.getUniqueNickname().length() < 40
                && dto.getAddress().length() < 500
                && dto.getModelName().length() < 40)
                ? Mono.just(dto) : Mono.error(badRequest());

    }

    public Mono<Tuple2<AedDeviceRegisterDto, SecurityContext>> validateUniqueNickname(Tuple2<AedDeviceRegisterDto, SecurityContext> tuple2) {
        return aedDeviceRepository.getAedDeviceByUniqNickname(tuple2.getT1().getUniqueNickname())
                .defaultIfEmpty(new AedDevice())
                .flatMap(aedDevice ->
                        aedDevice.getId() != null
                                ? Mono.error(badRequest("Device nickname already exists"))
                                : Mono.zip(Mono.just(tuple2.getT1()), Mono.just(tuple2.getT2()))
                );
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
