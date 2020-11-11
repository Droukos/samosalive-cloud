package com.droukos.cdnservice.model.factories;

import com.droukos.cdnservice.model.aed_device.AedDevice;
import com.droukos.cdnservice.model.user.UserRes;
import lombok.Getter;
import lombok.ToString;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@ToString
@Getter
public class AedDevicePicsFactory {
    private AedDevicePicsFactory() {}

    public static Mono<AedDevice> updateDevicePicsUrlMono(Tuple2<AedDevice, List<String>> tuple2) {
        return Mono.just(updateDevicePicsUrl(tuple2));
    }
    public static AedDevice updateDevicePicsUrl(Tuple2<AedDevice, List<String>> tuple2) {
        AedDevice aedDevice = tuple2.getT1();
       return new AedDevice(
               aedDevice.getId(),
               aedDevice.getNickname(),
               aedDevice.getModelName(),
               aedDevice.getDescr(),
               aedDevice.getAdded(),
               aedDevice.getAddedBy(),
               aedDevice.getCreated(),
               aedDevice.getStatus(),
               aedDevice.getStatusDescr(),
               aedDevice.getDefMap(),
               tuple2.getT2().get(0),
               tuple2.getT2().get(1),
               aedDevice.getAddr(),
               aedDevice.getOnMap(),
               aedDevice.getOnEvId(),
               aedDevice.getOnUId(),
               aedDevice.getTakenOn(),
               aedDevice.getOnEstFin()
       );
    }
}
