package com.droukos.aedservice.service.aed_event;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoSearch;
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

@Service
@RequiredArgsConstructor
public class AedEventInfo {
    @NonNull private final AedEventRepository aedEventRepository;

    public void validateType (AedEventDtoSearch aedEventDtoSearch){
        ValidatorUtil.validate(aedEventDtoSearch, new OccurrenceTypeValidator());
    }

    public Flux<AedEvent> findEventByType(AedEventDtoSearch aedEventDtoSearch) {
        return aedEventRepository.findAllByOccurrenceTypeIsLike(aedEventDtoSearch.getOccurrenceType());
                //.flatMap(fetchedEvent -> Mono.just(RequestedPreviewAedEvent.build(fetchedEvent)));
    }
    public Mono<RequestedPreviewAedEvent> fetchEventByType(AedEvent aedEvent){
        return Mono.just(RequestedPreviewAedEvent.build(aedEvent));
    }

}
