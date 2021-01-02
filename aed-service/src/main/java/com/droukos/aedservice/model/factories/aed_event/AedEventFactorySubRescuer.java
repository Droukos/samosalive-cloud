package com.droukos.aedservice.model.factories.aed_event;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoRescuerSub;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.UnionEventDeviceRescuerDto;
import com.droukos.aedservice.environment.enums.DeviceAvailability;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.model.user.ChannelSubs;
import com.droukos.aedservice.model.user.EventChannel;
import com.droukos.aedservice.model.user.UserRes;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.droukos.aedservice.environment.enums.AedEventStatus.ONPROGRESS;

public class AedEventFactorySubRescuer {

    private AedEventFactorySubRescuer() {
    }

    public static Mono<Tuple3<AedEvent, AedDevice, UserRes>> subAedRescuerAndDeviceMono(
            Tuple3<UnionEventDeviceRescuerDto, AedEventDtoRescuerSub, String > tuple3) {

        return Mono.zip(Mono.just(subRescuer(tuple3)), Mono.just(subAedDevice(tuple3)), Mono.just(subUserAsRescuer(tuple3)));
    }

    public static AedEvent subRescuer(Tuple3<UnionEventDeviceRescuerDto, AedEventDtoRescuerSub, String> tuple3) {
        AedEvent aedEvent = tuple3.getT1().getAedEvent();
        String rescuer = tuple3.getT2().getRescuer();
        return new AedEvent(
                aedEvent.getId(),
                aedEvent.getUsername(),
                aedEvent.getUsername_canon(),
                aedEvent.getOccurrenceType(),
                aedEvent.getOccurrencePoint(),
                tuple3.getT2().getAedDeviceId(),
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
                aedEvent.getSubs(),
                aedEvent.getCommsN()
        );
    }

    public static AedDevice subAedDevice(Tuple3<UnionEventDeviceRescuerDto, AedEventDtoRescuerSub, String> tuple3) {
        AedDevice aedDevice = tuple3.getT1().getAedDevice();
        AedEvent aedEvent = tuple3.getT1().getAedEvent();
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
                tuple3.getT2().getRescuer(),
                LocalDateTime.now(),
                tuple3.getT2().getEstimatedFinish()
        );
    }

    public static UserRes subUserAsRescuer(Tuple3<UnionEventDeviceRescuerDto, AedEventDtoRescuerSub, String> tuple3) {
        UserRes user = tuple3.getT1().getUserRes();
        String channelId = tuple3.getT3();
        Map<String, EventChannel> dbMap = user.getChannelSubs().getAedEvSubs();
        if (dbMap == null) {
            dbMap = new HashMap<>();
            dbMap.put(channelId, EventChannel.userAsRescuer());
        } else if (dbMap.containsKey(channelId)) {
            dbMap.replace(channelId, EventChannel.userAsRescuer());
        } else {
            dbMap.put(channelId, EventChannel.userAsRescuer());
        }
        return new UserRes(
                user.getId(),
                user.getUser(),
                user.getUserC(),
                user.getPass(),
                user.getPassC(),
                user.getEmail(),
                user.getEmailC(),
                user.getAllRoles(),
                user.getPrsn(),
                user.getPrivy(),
                user.getSys(),
                new ChannelSubs(dbMap, user.getChannelSubs().getAedPrSubs()),
                user.getAppState()
        );
    }
}
