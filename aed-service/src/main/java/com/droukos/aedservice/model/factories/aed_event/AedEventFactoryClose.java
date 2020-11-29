package com.droukos.aedservice.model.factories.aed_event;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoClose;
import com.droukos.aedservice.model.aed_event.AedEvent;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;

import static com.droukos.aedservice.environment.enums.AedEventStatus.COMPLETED;

public class AedEventFactoryClose {

    private AedEventFactoryClose() {}

    public static Mono<AedEvent> closeAedEvent(Tuple2<AedEvent, AedEventDtoClose> tuple2){
        return Mono.just(closeEvent(tuple2));
    }
    public static AedEvent closeEvent(Tuple2<AedEvent, AedEventDtoClose> tuple2){
        AedEvent aedEvent = tuple2.getT1();
        String conclusion = tuple2.getT2().getConclusion();
        return new AedEvent(
                aedEvent.getId(),
                aedEvent.getUsername(),
                aedEvent.getUsername_canon(),
                aedEvent.getOccurrenceType(),
                aedEvent.getOccurrencePoint(),
                aedEvent.getAddress(),
                aedEvent.getComment(),
                aedEvent.getRescuer(),
                aedEvent.getPhone(),
                COMPLETED.getStatus(),
                aedEvent.getRequestedTime(),
                aedEvent.getAcceptedTime(),
                LocalDateTime.now(),
                conclusion,
                aedEvent.getCallee()
        );
    }
}
