package com.droukos.aedservice.model.factories.aed_event;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoRescuerSub;
import com.droukos.aedservice.environment.enums.DeviceAvailability;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.model.aed_event.AedEvent;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import java.time.LocalDateTime;

import static com.droukos.aedservice.environment.enums.AedEventStatus.ONPROGRESS;

public class AedEventFactorySubRescuer {

    private AedEventFactorySubRescuer() {
    }

    public static Mono<Tuple2<AedEvent, AedDevice>> subAedRescuerAndDeviceMono(Tuple3<AedEvent, AedDevice, AedEventDtoRescuerSub> tuple3) {
        return Mono.zip(Mono.just(subRescuer(tuple3)), Mono.just(subAedDevice(tuple3)));
    }

    public static AedEvent subRescuer(Tuple3<AedEvent, AedDevice, AedEventDtoRescuerSub> tuple3) {
        AedEvent aedEvent = tuple3.getT1();
        String rescuer = tuple3.getT3().getRescuer();
        return new AedEvent(
                aedEvent.getId(),
                aedEvent.getUsername(),
                aedEvent.getUsername_canon(),
                aedEvent.getOccurrenceType(),
                aedEvent.getOccurrencePoint(),
                tuple3.getT3().getAedDeviceId(),
                aedEvent.getAddress(),
                aedEvent.getComment(),
                rescuer,
                aedEvent.getPhone(),
                ONPROGRESS.getStatus(),
                aedEvent.getRequestedTime(),
                LocalDateTime.now(),
                aedEvent.getCompletedTime(),
                aedEvent.getConclusion(),
                aedEvent.getCallee(),
                aedEvent.getSubs()
        );
    }

    public static AedDevice subAedDevice(Tuple3<AedEvent, AedDevice, AedEventDtoRescuerSub> tuple3) {
        AedDevice aedDevice = tuple3.getT2();
        AedEvent aedEvent = tuple3.getT1();
        return new AedDevice(
                aedDevice.getId(),
                aedDevice.getUniqNickname(),
                aedDevice.getModelName(),
                aedDevice.getDesc(),
                aedDevice.getAdded(),
                aedDevice.getAddedBy(),
                DeviceAvailability.BORROWED.getCode(),
                aedDevice.getStatusDesc(),
                aedDevice.getHomeP(),
                aedDevice.getPicUrl(),
                aedDevice.getAddrPicUrl(),
                aedDevice.getAddr(),
                aedEvent.getOccurrencePoint(),
                aedEvent.getId(),
                tuple3.getT3().getRescuer(),
                LocalDateTime.now(),
                aedDevice.getOnEstFin()
        );
    }
}
