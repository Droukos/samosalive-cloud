package com.droukos.aedservice.service.aed_event;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoCreate;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.repo.AedEventRepository;
import com.droukos.aedservice.service.validator.aed_event.AedCreationValidator;
import com.droukos.aedservice.util.ValidatorUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


import static com.droukos.aedservice.model.factories.AedEventFactoryCreate.eventCreate;

@Service
@RequiredArgsConstructor
public class AedEventCreation {
    @NonNull private final AedEventRepository aedEventRepository;

    public Mono<AedEvent> createAedEvent (AedEventDtoCreate aedEventDtoCreate){
        return Mono.just(eventCreate (aedEventDtoCreate) );
    }
    public void validateEvent (AedEventDtoCreate aedEventDtoCreate) {
        ValidatorUtil.validate(aedEventDtoCreate, new AedCreationValidator());
    }

    public Mono<Boolean> saveAedEvent(AedEvent event){
        return aedEventRepository.save(event).then(Mono.just(true));
    }
}
