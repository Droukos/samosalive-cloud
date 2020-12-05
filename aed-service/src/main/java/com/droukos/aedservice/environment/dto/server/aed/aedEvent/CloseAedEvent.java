package com.droukos.aedservice.environment.dto.server.aed.aedEvent;

import com.droukos.aedservice.model.aed_event.AedEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CloseAedEvent {
        private LocalDateTime completedTime;
        private String rescuer;
        private String conclusion;
    public static Mono<CloseAedEvent> buildMono(AedEvent aedEvent){
        return Mono.just(build(aedEvent));
    }

    public static CloseAedEvent build(AedEvent aedEvent) {
        return new CloseAedEvent(
                aedEvent.getCompletedTime(),
                aedEvent.getRescuer(),
                aedEvent.getConclusion());
    }
}
