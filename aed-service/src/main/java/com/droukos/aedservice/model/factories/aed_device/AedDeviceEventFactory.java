package com.droukos.aedservice.model.factories.aed_device;

import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceForEventDto;
import com.droukos.aedservice.model.aed_device.AedDevice;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;

public class AedDeviceEventFactory {
    private AedDeviceEventFactory() {}

    public static Mono<AedDevice> buildAedDeviceEventMono(Tuple2<AedDeviceForEventDto, AedDevice> tuple2) {
        return Mono.just(buildAedDeviceEvent(tuple2));
    }

    public static AedDevice buildAedDeviceEvent(Tuple2<AedDeviceForEventDto, AedDevice> tuple2) {
        AedDevice aedDevice = tuple2.getT2();
        AedDeviceForEventDto aedDeviceForEventDto = tuple2.getT1();
        return new AedDevice(
                aedDevice.getId(),
                aedDevice.getUniqNickname(),
                aedDevice.getModelName(),
                aedDevice.getDesc(),
                aedDevice.getAdded(),
                aedDevice.getAddedBy(),
                aedDevice.getStatus(),
                aedDevice.getStatusDesc(),
                aedDevice.getHomeP(),
                aedDevice.getPicUrl(),
                aedDevice.getAddrPicUrl(),
                aedDevice.getAddr(),
                aedDevice.getOnP(),
                aedDeviceForEventDto.getEventId(),
                aedDeviceForEventDto.getUserId(),
                LocalDateTime.now(),
                aedDevice.getOnEstFin()
        );
    }
}
