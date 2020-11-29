package com.droukos.cdnservice.model.factories;

import com.droukos.cdnservice.model.aed_device.AedDevice;
import lombok.Getter;
import lombok.ToString;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@ToString
@Getter
public class AedDevicePicsFactory {
    private AedDevicePicsFactory() {
    }

    public static Mono<AedDevice> updateDevicePicUrlMono(Tuple2<AedDevice, String> tuple2) {
       return Mono.just(updateDevicePicUrl(tuple2));
    }

    public static AedDevice updateDevicePicUrl(Tuple2<AedDevice, String> tuple2) {
        AedDevice aedDevice = tuple2.getT1();
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
                tuple2.getT2(),
                aedDevice.getAddrPicUrl(),
                aedDevice.getAddr(),
                aedDevice.getOnP(),
                aedDevice.getOnEvId(),
                aedDevice.getOnUId(),
                aedDevice.getTakenOn(),
                aedDevice.getOnEstFin()
        );
    }

    public static Mono<AedDevice> updateDeviceAddressPicUrlMono(Tuple2<AedDevice, String> tuple2) {
        return Mono.just(updateDeviceAddressPicUrl(tuple2));
    }

    public static AedDevice updateDeviceAddressPicUrl(Tuple2<AedDevice, String> tuple2) {
        AedDevice aedDevice = tuple2.getT1();
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
                tuple2.getT2(),
                aedDevice.getAddr(),
                aedDevice.getOnP(),
                aedDevice.getOnEvId(),
                aedDevice.getOnUId(),
                aedDevice.getTakenOn(),
                aedDevice.getOnEstFin()
        );
    }

    public static Mono<AedDevice> updateDevicePicsUrlMono(Tuple2<AedDevice, List<String>> tuple2) {
        return Mono.just(updateDevicePicsUrl(tuple2));
    }

    public static AedDevice updateDevicePicsUrl(Tuple2<AedDevice, List<String>> tuple2) {
        AedDevice aedDevice = tuple2.getT1();
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
                tuple2.getT2().get(0),
                tuple2.getT2().get(1),
                aedDevice.getAddr(),
                aedDevice.getOnP(),
                aedDevice.getOnEvId(),
                aedDevice.getOnUId(),
                aedDevice.getTakenOn(),
                aedDevice.getOnEstFin()
        );
    }
}
