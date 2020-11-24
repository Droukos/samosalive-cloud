package com.droukos.aedservice.service.aed_event;

import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.repo.AedEventRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Getter
public class AedEventServices {

    @NonNull private final AedEventRepository aedEventRepository;

    public Flux<AedEvent> getEventsByOccurrence(Integer occurrence) {
        return aedEventRepository.findAedEventsByOccurrenceType(occurrence);
    }

    //public Mono<AedEvent> validateComment (ServerRequest request){
    //    return request.bodyToMono(AedEvent.class)
    //            .defaultIfEmpty(new AedEvent())
    //            .flatMap(event -> {
    //                ValidatorUtil.validate(event, new AedCreationValidator());
    //                return Mono.just(event);
    //            });
    //}
//
    //public Mono<AedEvent> validateType (ServerRequest request){
    //    return request.bodyToMono(AedEvent.class)
    //            .defaultIfEmpty(new AedEvent())
    //            .flatMap(event -> {
    //                ValidatorUtil.validate(event, new OccurrenceTypeValidator());
    //                return Mono.just(event);
    //            });
    //}

    public Flux<AedEvent> getEventsByRescuer(String rescuer){
        return aedEventRepository.findAllByRescuerLike(rescuer);
    }

    public Flux<AedEvent> getAllEvents() {
        return aedEventRepository.findAll();
    }

    public Mono<AedEvent> createEventDto(ServerRequest request){
        return request.bodyToMono(AedEvent.class).defaultIfEmpty(new AedEvent());
    }
}
