package com.droukos.aedservice.environment.dto.server.aed.aedEvent;

import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.util.GeoJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class RequestedAedEvent {
    private String id;
    private String username;
    private Integer occurrenceType;
    @JsonDeserialize(using = GeoJsonDeserializer.class)
    private Point occurrencePoint;
    private String aedDeviceId;
    private String address;
    private String comment;
    private Integer status;
    private LocalDateTime requestedTime;
    private LocalDateTime completedTime;
    private String rescuer;
    private String phone;
    private String callee;
    private String conclusion;


    public static Mono<RequestedAedEvent> buildMono(AedEvent aedEvent){
        return Mono.just(build(aedEvent));
    }

    public static RequestedAedEvent build(AedEvent aedEvent) {
        return new RequestedAedEvent(
                aedEvent.getId(),
                aedEvent.getUsername(),
                aedEvent.getOccurrenceType(),
                aedEvent.getOccurrencePoint(),
                aedEvent.getAedDeviceId(),
                aedEvent.getAddress(),
                aedEvent.getComment(),
                aedEvent.getStatus(),
                aedEvent.getRequestedTime(),
                aedEvent.getCompletedTime(),
                aedEvent.getRescuer(),
                aedEvent.getPhone(),
                aedEvent.getCallee(),
                aedEvent.getConclusion());
    }
}
