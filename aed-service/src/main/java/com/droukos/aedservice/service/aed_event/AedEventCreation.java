package com.droukos.aedservice.service.aed_event;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoCreate;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.repo.AedEventRepository;
import com.droukos.aedservice.service.validator.aed_event.AedCreationValidator;
import com.droukos.aedservice.util.ValidatorUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AedEventCreation {
    private final AedEventRepository aedEventRepository;

    public void validateEvent(AedEventDtoCreate aedEventDtoCreate) {
        ValidatorUtil.validate(aedEventDtoCreate, new AedCreationValidator());
    }

    public Mono<AedEvent> saveAedEvent(AedEvent event) {
        return aedEventRepository.save(event);
    }


}
