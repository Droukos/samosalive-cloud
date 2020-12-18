package com.droukos.aedservice.controller;

import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceAreaSearchDto;
import com.droukos.aedservice.environment.dto.client.aed_event.*;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.CloseAedEvent;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.RequestedAedEvent;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.RequestedPreviewAedEvent;
import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceInfoPreviewDto;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.model.factories.aed_event.AedEventFactoryClose;
import com.droukos.aedservice.model.factories.aed_event.AedEventFactoryCreate;
import com.droukos.aedservice.model.factories.aed_event.AedEventFactorySubRescuer;
import com.droukos.aedservice.service.aed_event.AedEventChannel;
import com.droukos.aedservice.service.aed_event.AedEventCreation;
import com.droukos.aedservice.service.aed_event.AedEventInfo;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class AedEventController {

    private final AedEventCreation aedEventCreation;
    private final AedEventChannel aedEventChannel;
    private final AedEventInfo aedEventInfo;

    @MessageMapping("aed.event.post")
    public Mono<String> createEvent(AedEventDtoCreate aedEventDtoCreate) {
        return Mono.just(aedEventDtoCreate)
                .doOnNext(aedEventCreation::validateEvent)
                .flatMap(AedEventFactoryCreate::aedEventCreateMono)
                .flatMap(aedEventCreation::saveAedEvent)
                .flatMap(aedEventChannel::publishEventOnRedisChannel)
                .flatMap(aedEvent -> Mono.just(aedEvent.getId()));
    }

    @MessageMapping("aed.event.listen.sub")
    public Flux<AedEvent> listenSubEvent(AedEventDtoIdSearch aedEventDtoIdSearch) {
        return aedEventInfo.findEventId(aedEventDtoIdSearch.getId())
                .zipWith(ReactiveSecurityContextHolder.getContext())
                .flatMap(aedEventChannel::validateListenerForEvent)
                .flatMap(aedEventChannel::checkAndInsertUserSubOnDb)
                .flatMap(aedEventChannel::checkAndInsertAedEventSubOnDb)
                .flatMapMany(aedEventChannel::fetchPublishedAedEventsFromSingle);
    }

    @MessageMapping("aed.event.post.comment")
    public Mono<Boolean> eventPostComment() {

        return Mono.empty();
    }

    @MessageMapping("aed.event.listen")
    public Flux<RequestedPreviewAedEvent> listenEvents() {
        return aedEventChannel.fetchPublishedAedEvents()
                .flatMap(RequestedPreviewAedEvent::buildMono);
    }

    @MessageMapping("aed.event.get")
    public Flux<RequestedPreviewAedEvent> findEvent(AedEventDtoSearch aedEventDtoSearch) {
        return Flux.just(aedEventDtoSearch)
                .doOnNext(aedEventInfo::validateType)
                .flatMap(aedEventInfo::findEventOnFilter)
                .flatMap(RequestedPreviewAedEvent::buildMono);
    }

    @MessageMapping("aed.event.fetch.pending")
    public Flux<RequestedPreviewAedEvent> findUnassignedEvents() {
        return (aedEventInfo.findUnassignedEvents())
                .flatMap(RequestedPreviewAedEvent::buildMono);
    }

    @MessageMapping("aed.event.getId")
    public Mono<RequestedAedEvent> findEventId(AedEventDtoIdSearch aedEventDtoIdSearch) {
        return Mono.just(aedEventDtoIdSearch.getId())
                .flatMap(aedEventInfo::findEventId)
                .flatMap(RequestedAedEvent::buildMono);
    }

    //@MessageMapping("aed.event.fetch.device.inArea")
    //public Flux<AedDeviceInfoPreviewDto> fetchDevicesInArea(AedEventDtoIdSearch aedEventDtoIdSearch) {
//
    //    return Flux.just(aedEventDtoIdSearch.getId())
    //            .flatMap(aedDeviceInfo::validateAedDeviceMaxDistance)
    //            .flatMap(aedDeviceInfo::findAedDeviceInArea)
    //            .flatMap(AedDeviceInfoPreviewDto::buildMono);
    //}

    @MessageMapping("aed.event.subRescuer")
    public Mono<AedEvent> setEventRescuer(AedEventDtoRescuerSub aedEventDtoRescuerSub) {
        return ReactiveSecurityContextHolder.getContext()
                .zipWith(Mono.just(aedEventDtoRescuerSub))
                .flatMap(aedEventInfo::fetchRescuerFromDtoOrContext)
                .flatMap(aedEventInfo::findEventByIdForRescuerSub)
                .flatMap(aedEventInfo::findDeviceByIdForRescuing)
                .flatMap(aedEventInfo::validateAedDeviceForRescuing)
                .flatMap(AedEventFactorySubRescuer::subAedRescuerAndDeviceMono)
                .flatMap(aedEventInfo::saveAedEvent)
                .flatMap(aedEventInfo::saveAedDevice)
                .flatMap(aedEventChannel::publishEventOnRedisChannel)
                .flatMap(aedEventChannel::publishEventOnRedisSingleChannel);
    }

    @MessageMapping("aed.event.close")
    public Mono<CloseAedEvent> closeAedEvent(AedEventDtoClose aedEventDtoClose) {
        return aedEventInfo.findEventId(aedEventDtoClose.getId())
                .zipWith(Mono.just(aedEventDtoClose))
                .flatMap(aedEventInfo::fetchAedDeviceForClosingEvent)
                .flatMap(AedEventFactoryClose::closeAedEvent)
                .flatMap(aedEventInfo::saveAedDevice)
                .flatMap(aedEventInfo::saveAedEvent)
                .flatMap(aedEventChannel::publishEventOnRedisSingleChannel)
                .flatMap(aedEventChannel::removeUsersChannelSubFromDb)
                .flatMap(CloseAedEvent::buildMono);
    }
}
