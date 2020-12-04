package com.droukos.aedservice.service.aed_event;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoSearch;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.RequestedAedEvent;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.RequestedPreviewAedEvent;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.repo.AedEventRepository;
import com.droukos.aedservice.service.validator.aed_event.OccurrenceTypeValidator;
import com.droukos.aedservice.util.ValidatorUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.droukos.aedservice.environment.constants.AedStatusCodes.SDISABLED;
import static com.droukos.aedservice.environment.constants.AedTypeCodes.TDISABLED;
import static com.droukos.aedservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@RequiredArgsConstructor
public class AedEventInfo {
    @NonNull private final AedEventRepository aedEventRepository;

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

    public Mono<AedEvent> saveAndReturnAedEvent(AedEvent aedEvent){
        return aedEventRepository.save(aedEvent);
    }

    public Mono<RequestedAedEvent> fetchEventByType(AedEvent aedEvent){
        return Mono.just(RequestedAedEvent.build(aedEvent));
    }

    public Mono<RequestedPreviewAedEvent> fetchPreviewEventByType(AedEvent aedEvent){
        return Mono.just(RequestedPreviewAedEvent.build(aedEvent));
    }

}
