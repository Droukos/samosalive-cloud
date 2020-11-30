package com.droukos.aedservice.environment.dto.server.aed.aedEvent;

import com.droukos.aedservice.model.aed_event.AedEvent;
import lombok.*;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class RequestedPreviewAedEvent {
    private String id;
    private String username;
    private Integer occurrenceType;
    private GeoJsonPoint occurrencePoint;
    private String address;
    private String comment;
    private Integer status;
    private LocalDateTime requestedTime;

    public static Mono<RequestedPreviewAedEvent> buildMono(AedEvent aedEvent){
        return Mono.just(build(aedEvent));
    }

    public static RequestedPreviewAedEvent build(AedEvent aedEvent) {
        return new RequestedPreviewAedEvent(
                aedEvent.getId(),
                aedEvent.getUsername(),
                aedEvent.getOccurrenceType(),
                aedEvent.getOccurrencePoint(),
                aedEvent.getAddress(),
                aedEvent.getComment(),
                aedEvent.getStatus(),
                aedEvent.getRequestedTime());
    }
}
