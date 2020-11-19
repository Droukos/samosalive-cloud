package com.droukos.aedservice.model.factories.aed_event;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoRescuerSub;
import com.droukos.aedservice.model.aed_event.AedEvent;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;

import static com.droukos.aedservice.environment.enums.AedEventStatus.ONPROGRESS;

public class AedEventFactorySubRescuer {

    private AedEventFactorySubRescuer() {}

    public static Mono<AedEvent> subRescuerMono(Tuple2<AedEvent, AedEventDtoRescuerSub> tuple2){
        return Mono.just(subRescuer(tuple2));
    }
    public static AedEvent subRescuer(Tuple2<AedEvent, AedEventDtoRescuerSub> tuple2){
        AedEvent aedEvent = tuple2.getT1();
        String rescuer = tuple2.getT2().getRescuer();
        return new AedEvent(
                aedEvent.getId(),
                aedEvent.getUsername(),
                aedEvent.getUsername_canon(),
                aedEvent.getOccurrenceType(),
                aedEvent.getOccurrencePoint(),
                aedEvent.getAddress(),
                aedEvent.getComment(),
                rescuer,
                aedEvent.getPhone(),
                ONPROGRESS.getStatus(),
                aedEvent.getRequestedTime(),
                LocalDateTime.now(),
                aedEvent.getCallee()
        );
    }
}
