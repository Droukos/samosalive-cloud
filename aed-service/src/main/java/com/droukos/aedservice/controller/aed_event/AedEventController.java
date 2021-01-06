package com.droukos.aedservice.controller.aed_event;

import com.droukos.aedservice.environment.dto.client.aed_event.*;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.EventPreviewUsersDto;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.RequestedAedEvent;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.RequestedPreviewAedEvent;
import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceRescuer;
import com.droukos.aedservice.model.factories.aed_event.AedEventFactoryCreate;
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
                .flatMap(aedEventInfo::findEventById)
                .flatMap(RequestedAedEvent::buildMono);
    }

    @MessageMapping("aed.event.fetch.rescuer.and.device")
    public Flux<AedDeviceRescuer> fetchRescuerAndDevice(AedEventRescuerDeviceSearch dto) {
        return aedEventInfo.fetchDeviceRescuer(dto.getAedDeviceId());
    }

    @MessageMapping("aed.event.fetch.preview.users")
    public Mono<EventPreviewUsersDto> fetchEventSubUsers(AedEventDtoIdSearch aedEventDtoIdSearch) {
        return Mono.just(aedEventDtoIdSearch.getId())
                .flatMap(aedEventInfo::fetchEventUsers)
                .zipWith(ReactiveSecurityContextHolder.getContext())
                .flatMap(aedEventInfo::validateAndServeEventUsers);
    }
}
