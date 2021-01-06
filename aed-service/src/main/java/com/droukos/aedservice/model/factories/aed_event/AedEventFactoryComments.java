package com.droukos.aedservice.model.factories.aed_event;

import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.model.aed_event.AedEventComment;
import reactor.core.publisher.Mono;

import static com.droukos.aedservice.environment.enums.AedEventStatus.ONPROGRESS;

public class AedEventFactoryComments {
    private AedEventFactoryComments() {}

    public static Mono<AedEvent> add1CommNumOnEventMono(AedEvent aedEvent) {
        return Mono.just(add1CommNumOnEvent(aedEvent));
    }

    public static AedEvent add1CommNumOnEvent(AedEvent aedEvent) {
        return new AedEvent(
                aedEvent.getId(),
                aedEvent.getUsername(),
                aedEvent.getUsername_canon(),
                aedEvent.getOccurrenceType(),
                aedEvent.getOccurrencePoint(),
                aedEvent.getAedDeviceId(),
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
                aedEvent.getSubs(),
                aedEvent.getCommsN() + 1
        );
    }

    public static AedEventComment addFieldAllCommsNum(AedEventComment aedEventComment) {
        return new AedEventComment(
                aedEventComment.getId(),
                aedEventComment.getEventId(),
                aedEventComment.getUsername(),
                aedEventComment.getMessage(),
                aedEventComment.getPosted(),
                aedEventComment.getAllComments()
        );
    }
}
