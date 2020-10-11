package com.droukos.aedservice.model.aed_event;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJson;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor @NoArgsConstructor
@Getter @Document
public class AedEvent {

    @Id
    private String id;

    @Indexed
    private String username;

    private String username_canon;

    @Indexed
    private String occurrenceType;

    private GeoJson occurrence_point;

    private String address;

    private String comment;

    private String rescuer;

    private String phone;//αυτός που κάνει την αίτηση, είτε είναι ο ίδιος είτε έχει ανατεθεί αλλού

    private String status;

    private LocalDateTime requested_time;

    private LocalDateTime accepted_time;

    private String callee;
}
