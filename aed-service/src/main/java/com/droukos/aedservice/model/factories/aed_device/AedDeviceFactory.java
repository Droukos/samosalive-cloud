package com.droukos.aedservice.model.factories.aed_device;

import com.droukos.aedservice.environment.dto.RequesterAccessTokenData;
import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceEditDto;
import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceRegisterDto;
import com.droukos.aedservice.environment.enums.DeviceAvailability;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.util.SecurityUtil;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
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
                aedDeviceRegisterDto.getUniqueNickname(),
                aedDeviceRegisterDto.getModelName(),
                aedDeviceRegisterDto.getDescription(),
                LocalDateTime.now(),
                requesterData.getUserId(),
                DeviceAvailability.AVAILABLE.getCode(),
                null,
                new GeoJsonPoint(aedDeviceRegisterDto.getDefaultMapX(), aedDeviceRegisterDto.getDefaultMapY()),
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

    public static Mono<AedDevice> buildEditedDeviceMono(Tuple2<AedDeviceEditDto, AedDevice> tuple2) {
        return Mono.just(buildEditedDevice(tuple2));
    }

    public static AedDevice buildEditedDevice(Tuple2<AedDeviceEditDto, AedDevice> tuple2) {
        AedDevice aedDevice = tuple2.getT2();
        AedDeviceEditDto aedDeviceEditDto = tuple2.getT1();
        return new AedDevice(
                aedDevice.getId(),
                aedDevice.getUniqNickname(),
                aedDeviceEditDto.getModelName(),
                aedDeviceEditDto.getModelDescription(),
                aedDevice.getAdded(),
                aedDevice.getAddedBy(),
                aedDevice.getStatus(),
                aedDevice.getStatusDesc(),
                new GeoJsonPoint(aedDeviceEditDto.getHomePointX(), aedDeviceEditDto.getHomePointY()),
                aedDevice.getPicUrl(),
                aedDevice.getAddrPicUrl(),
                aedDeviceEditDto.getAddress(),
                aedDevice.getOnP(),
                aedDevice.getOnEvId(),
                aedDevice.getOnUId(),
                aedDevice.getTakenOn(),
                aedDevice.getOnEstFin()
        );
    }
}
