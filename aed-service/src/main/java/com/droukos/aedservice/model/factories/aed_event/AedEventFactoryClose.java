package com.droukos.aedservice.model.factories.aed_event;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoClose;
import com.droukos.aedservice.environment.enums.DeviceAvailability;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.model.aed_event.AedEvent;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import java.time.LocalDateTime;

import static com.droukos.aedservice.environment.enums.AedEventStatus.COMPLETED;

public class AedEventFactoryClose {

    private AedEventFactoryClose() {}

    public static Mono<Tuple2<AedEvent, AedDevice>> closeAedEvent(Tuple3<AedEvent, AedDevice, AedEventDtoClose> tuple3){
        return Mono.zip(Mono.just(closeEvent(tuple3)), Mono.just(returnAedDevice(tuple3)));
    }
    public static AedEvent closeEvent(Tuple3<AedEvent, AedDevice, AedEventDtoClose> tuple3){
        AedEvent aedEvent = tuple3.getT1();
        String conclusion = tuple3.getT3().getConclusion();
        return new AedEvent(
                aedEvent.getId(),
                aedEvent.getUsername(),
                aedEvent.getUsername_canon(),
                aedEvent.getOccurrenceType(),
                aedEvent.getOccurrencePoint(),
                aedEvent.getAedDeviceId(),
                aedEvent.getAddress(),
                aedEvent.getComment(),
                aedEvent.getRescuer(),
                aedEvent.getPhone(),
                COMPLETED.getStatus(),
                aedEvent.getRequestedTime(),
                aedEvent.getAcceptedTime(),
                LocalDateTime.now(),
                conclusion,
                aedEvent.getCallee(),
                aedEvent.getSubs(),
                aedEvent.getCommsN()
        );
    }

    public static AedDevice returnAedDevice(Tuple3<AedEvent, AedDevice, AedEventDtoClose> tuple3) {
        AedDevice aedDevice = tuple3.getT2();
        return new AedDevice(
                aedDevice.getId(),
                aedDevice.getUniqNickname(),
                aedDevice.getModelName(),
                aedDevice.getDesc(),
                aedDevice.getAdded(),
                aedDevice.getAddedBy(),
                DeviceAvailability.RETURNING.getCode(),
                aedDevice.getStatusDesc(),
                aedDevice.getHomeP(),
                aedDevice.getPicUrl(),
                aedDevice.getAddrPicUrl(),
                aedDevice.getAddr(),
                aedDevice.getOnP(),
                aedDevice.getOnEvId(),
                aedDevice.getOnUId(),
                aedDevice.getTakenOn(),
                aedDevice.getOnEstFin()
        );
    }
}
