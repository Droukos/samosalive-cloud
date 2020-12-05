package com.droukos.aedservice.service.aed_event;

import com.droukos.aedservice.config.redis.RedisConfigProperties;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoSearch;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.RequestedPreviewAedEvent;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.repo.AedEventRepository;
import com.droukos.aedservice.service.validator.aed_event.OccurrenceTypeValidator;
import com.droukos.aedservice.util.ValidatorUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.droukos.aedservice.environment.constants.AedStatusCodes.SDISABLED;
import static com.droukos.aedservice.environment.constants.AedTypeCodes.TDISABLED;
import static com.droukos.aedservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@AllArgsConstructor
public class AedEventInfo {
    private final AedEventRepository aedEventRepository;
    private final RedisConfigProperties redisConfigProperties;
    private final ReactiveRedisTemplate<String, AedEvent> reactiveRedisTemplate;

    public void validateType (AedEventDtoSearch aedEventDtoSearch){
        ValidatorUtil.validate(aedEventDtoSearch, new OccurrenceTypeValidator());
    }

    public Flux<AedEvent> findEventOnFilter(AedEventDtoSearch aedEventDtoSearch) {
        if(aedEventDtoSearch.getOccurrenceType()==TDISABLED&&aedEventDtoSearch.getStatus()!=SDISABLED){
            return aedEventRepository.findAedEventsByStatus(aedEventDtoSearch.getStatus());
        }
        else if(aedEventDtoSearch.getOccurrenceType()!=TDISABLED&&aedEventDtoSearch.getStatus()==SDISABLED){
            return aedEventRepository.findAedEventsByOccurrenceType(aedEventDtoSearch.getOccurrenceType());
        }
        else{
            return aedEventRepository.findAedEventsByOccurrenceTypeAndStatus(aedEventDtoSearch.getOccurrenceType(),aedEventDtoSearch.getStatus());
        }
    }
    public Mono<AedEvent> findEventId(String id) {
        return aedEventRepository.findById(id)
                .defaultIfEmpty(new AedEvent())
                .flatMap(aedEvent -> aedEvent.getId() == null ? Mono.error(badRequest("Event not found")) : Mono.just(aedEvent));
    }

    public Mono<Void> saveAedEvent(AedEvent aedEvent){
        return aedEventRepository.save(aedEvent).then(Mono.empty());
    }

    public Mono<RequestedPreviewAedEvent> fetchEventByType(AedEvent aedEvent){
        return Mono.just(RequestedPreviewAedEvent.build(aedEvent));
    }

    public Flux<AedEvent> fetchPublishedAedEvents() {
        return this.reactiveRedisTemplate
                .listenToChannel(redisConfigProperties.getAedEventLiveChannel())
                .flatMap(reactiveSubMsg -> Mono.just(reactiveSubMsg.getMessage()));
    }

}
