package com.droukos.aedservice.model.factories;

import com.droukos.aedservice.environment.dto.RequesterAccessTokenData;
import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceRegisterDto;
import com.droukos.aedservice.environment.enums.DeviceAvailability;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.service.aed_device.AedDeviceRegister;
import com.droukos.aedservice.util.SecurityUtil;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;

public class AedDeviceFactory {
    private AedDeviceFactory() {}

    public static Mono<AedDevice> buildDeviceMono(Tuple2<AedDeviceRegisterDto, SecurityContext> tuple2){
        return Mono.just(buildDevice(tuple2));
    }
    public static AedDevice buildDevice(Tuple2<AedDeviceRegisterDto, SecurityContext> tuple2) {
        RequesterAccessTokenData requesterData = SecurityUtil.getRequesterData(tuple2.getT2());
        AedDeviceRegisterDto aedDeviceRegisterDto = tuple2.getT1();

        return new AedDevice(
                null,
                aedDeviceRegisterDto.getNickname(),
                aedDeviceRegisterDto.getModelName(),
                aedDeviceRegisterDto.getDescription(),
                LocalDateTime.now(),
                requesterData.getUserId(),
                LocalDateTime.now(),
                DeviceAvailability.AVAILABLE.getCode(),
                null,
                aedDeviceRegisterDto.getDefaultMap(),
                null,
                null,
                aedDeviceRegisterDto.getAddress(),
                null,
                null,
                null,
                null,
                0
        );
    }
}
