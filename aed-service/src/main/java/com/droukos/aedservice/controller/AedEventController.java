package com.droukos.aedservice.controller;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoCreate;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoSearch;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.RequestedPreviewAedEvent;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.service.aed_event.AedEventCreation;
import com.droukos.aedservice.service.aed_event.AedEventInfo;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class AedEventController {

    //@MessageMapping("aed.event.getAllEvents")
    //public Flux<AedEvent> getAllEvents(){
    //}
    private final AedEventCreation aedEventCreation;
    private final AedEventInfo aedEventInfo;

    @MessageMapping("aed.event.post")
    public Mono<Boolean> createEvent(AedEventDtoCreate aedEventDtoCreate){
        return Mono.just(aedEventDtoCreate)
                .doOnNext(aedEventCreation::validateEvent)
                .flatMap(aedEventCreation::createAedEvent)
                .flatMap(aedEventCreation::saveAedEvent);
    }

    @MessageMapping("aed.event.get")
    public Flux<RequestedPreviewAedEvent> findEvent(AedEventDtoSearch aedEventDtoSearch){
        return Flux.just(aedEventDtoSearch)
                .doOnNext(aedEventInfo::validateType)
                .flatMap(aedEventInfo::findEventByType)
                .flatMap(aedEventInfo::fetchEventByType);
    }

    //@MessageMapping("aed.event.getEventLike")
    //public Mono<AedEvent> getEventLike(){
//
    //}

    //public Mono<ServerResponse> getAllEvents(ServerRequest serverRequest) {
    //    return ok()
    //            .contentType(APPLICATION_JSON)
    //            .body(aedEventServices.getAllEvents(), Event.class);
    //}
//
//
    //public Mono<ServerResponse> createEvent(ServerRequest request) {
    //    return aedEventCreation.validateCreateEvent(request)
    //            .flatMap(aedEventCreation::saveAedEvent);
    //}
//
    //public Mono<ServerResponse> getEventLike(ServerRequest request) {
    //    return aedEventServices.createEventDto(request)
    //            .flatMap(aedEventInfo::validateType)
    //            .flatMap(aedEventInfo::fetchEventByType);
    //}
}
