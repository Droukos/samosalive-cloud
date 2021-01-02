package com.droukos.aedservice.model.factories.aed_device;

import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceForEventDto;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.model.aed_event.AedEvent;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import java.time.LocalDateTime;

public class AedDeviceEventFactory {
    private AedDeviceEventFactory() {
    }

    public static Mono<AedDevice> buildAedDeviceEventMono(Tuple3<AedDeviceForEventDto, AedDevice, AedEvent> tuple3) {
        return Mono.just(buildAedDeviceEvent(tuple3));
    }

    public static AedDevice buildAedDeviceEvent(Tuple3<AedDeviceForEventDto, AedDevice, AedEvent> tuple3) {
        AedDevice aedDevice = tuple3.getT2();
        AedDeviceForEventDto aedDeviceForEventDto = tuple3.getT1();
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

    public static Mono<AedEvent> buildAedEventMono(Tuple3<AedDeviceForEventDto, AedDevice, AedEvent> tuple3) {
        return Mono.just(buildAedEvent(tuple3));
    }

    public static AedEvent buildAedEvent(Tuple3<AedDeviceForEventDto, AedDevice, AedEvent> tuple3) {
        AedEvent aedEvent = tuple3.getT3();
        AedDeviceForEventDto dto = tuple3.getT1();

        return new AedEvent(
                aedEvent.getId(),
                aedEvent.getUsername(),
                aedEvent.getUsername_canon(),
                aedEvent.getOccurrenceType(),
                aedEvent.getOccurrencePoint(),
                dto.getAedDeviceId(),
                aedEvent.getAddress(),
                aedEvent.getComment(),
                aedEvent.getRescuer(),
                aedEvent.getPhone(),
                aedEvent.getStatus(),
                aedEvent.getRequestedTime(),
                aedEvent.getAcceptedTime(),
                aedEvent.getCompletedTime(),
                aedEvent.getConclusion(),
                aedEvent.getCallee(),
                aedEvent.getSubs(),
                aedEvent.getCommsN()
        );
    }

    public static Mono<Tuple2<AedDevice, AedEvent>> buildZippedAedDeviceAndEvent(Tuple3<AedDeviceForEventDto, AedDevice, AedEvent> tuple3) {
        return Mono.zip(buildAedDeviceEventMono(tuple3), buildAedEventMono(tuple3));
    }
}
