package com.droukos.aedservice.service.aed_event;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoCreate;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.model.sub.UserSub;
import com.droukos.aedservice.repo.AedEventRepository;
import com.droukos.aedservice.repo.UserSubsRepository;
import com.droukos.aedservice.service.validator.aed_event.AedCreationValidator;
import com.droukos.aedservice.util.ValidatorUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@AllArgsConstructor
public class AedEventCreation {
    private final AedEventRepository aedEventRepository;
    private final UserSubsRepository userSubsRepository;

    public void validateEvent(AedEventDtoCreate aedEventDtoCreate) {
        ValidatorUtil.validate(aedEventDtoCreate, new AedCreationValidator());
    }

    public Mono<AedEvent> saveAedEventAndSub(Tuple2<AedEvent, UserSub> tuple2) {
        AedEvent aedEvent = tuple2.getT1();
        return aedEventRepository.save(aedEvent)
                .then(userSubsRepository.save(tuple2.getT2()))
                .then(Mono.just(aedEvent));
    }
}