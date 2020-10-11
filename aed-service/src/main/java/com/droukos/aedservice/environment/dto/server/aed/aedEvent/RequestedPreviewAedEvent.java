package com.droukos.aedservice.environment.dto.server.aed.aedEvent;

import com.droukos.aedservice.model.aed_event.AedEvent;
import lombok.*;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class RequestedPreviewAedEvent {
    private String id;
    private String user;
    private String occ;
    private String addr;
    private String comm;
    private String status;

    public static RequestedPreviewAedEvent build(AedEvent aedEvent) {
        return new RequestedPreviewAedEvent(
                aedEvent.getId(),
                aedEvent.getUsername(),
                aedEvent.getOccurrenceType(),
                aedEvent.getAddress(),
                aedEvent.getComment(),
                aedEvent.getStatus());
    }
}
