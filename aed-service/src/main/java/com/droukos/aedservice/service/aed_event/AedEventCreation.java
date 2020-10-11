package com.droukos.aedservice.service.aed_event;

import com.droukos.aedservice.environment.constants.StatusCodes;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoCreate;
import com.droukos.aedservice.environment.dto.server.ApiResponse;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.repo.AedEventRepository;
import com.droukos.aedservice.service.validator.aed_event.AedCreationValidator;
import com.droukos.aedservice.util.ValidatorUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static com.droukos.aedservice.model.factories.AedEventFactoryCreate.eventCreate;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

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
    public Mono<ServerResponse> saveAedEvent(AedEvent event){
        Function<AedEvent, Mono<ServerResponse>> result = savedEvent -> ok().contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(new ApiResponse(StatusCodes.OK, "Event created", "event.created")));

        return aedEventRepository.save(event).flatMap(result);
    }
}
