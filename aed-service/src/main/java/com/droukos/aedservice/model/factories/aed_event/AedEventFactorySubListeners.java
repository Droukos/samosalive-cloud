package com.droukos.aedservice.model.factories.aed_event;

import com.droukos.aedservice.model.aed_event.AedEvent;
import reactor.util.function.Tuple2;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AedEventFactorySubListeners {
    private AedEventFactorySubListeners() {}

    public static AedEvent buildAedEventWithListener(Tuple2<AedEvent, String> tuple2) {
        AedEvent aedEvent = tuple2.getT1();
        String username = tuple2.getT2();
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
                aedEvent.getStatus(),
                aedEvent.getRequestedTime(),
                aedEvent.getAcceptedTime(),
                aedEvent.getCompletedTime(),
                aedEvent.getConclusion(),
                aedEvent.getCallee(),
                Stream.concat(aedEvent.getSubs().stream(), Stream.of(username)).collect(Collectors.toList())
        );
    }
}
