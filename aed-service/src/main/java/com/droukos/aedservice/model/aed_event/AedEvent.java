package com.droukos.aedservice.model.aed_event;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

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
    private Integer occurrenceType;

    private GeoJsonPoint occurrencePoint;

    private String address;

    private String comment;

    //@Indexed
    //private String aedId;

    private String rescuer;

    private String phone;//αυτός που κάνει την αίτηση, είτε είναι ο ίδιος είτε έχει ανατεθεί αλλού

    private Integer status;

    private LocalDateTime requestedTime;

    private LocalDateTime acceptedTime;

    private LocalDateTime completedTime;

    private String conclusion;

    private String callee;
}
