package com.droukos.aedservice.service.aed_event;

import com.droukos.aedservice.environment.dto.server.aed.aedEvent.RequestedPreviewAedEvent;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.repo.AedEventRepository;
import com.droukos.aedservice.service.validator.aed_event.OccurrenceTypeValidator;
import com.droukos.aedservice.util.ValidatorUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@RequiredArgsConstructor
public class AedEventInfo {
    @NonNull private final AedEventRepository aedEventRepository;

    public void validateType (String type){
        ValidatorUtil.validate(type, new OccurrenceTypeValidator());
    }

    public Flux<AedEvent> findEventByType(String type) {
        return aedEventRepository.findAllByOccurrenceTypeIsLike(type);
                //.flatMap(fetchedEvent -> Mono.just(RequestedPreviewAedEvent.build(fetchedEvent)));
    }
    public Mono<RequestedPreviewAedEvent> fetchEventByType(AedEvent aedEvent){
        return Mono.just(RequestedPreviewAedEvent.build(aedEvent));
    }

}
