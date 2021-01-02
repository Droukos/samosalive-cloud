package com.droukos.aedservice.model.aed_event;

import com.droukos.aedservice.util.GeoJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

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

    @JsonDeserialize(using = GeoJsonDeserializer.class)
    private GeoJsonPoint occurrencePoint;

    private String aedDeviceId;

    private String address;

    private String comment;

    private String rescuer;

    private String phone;//αυτός που κάνει την αίτηση, είτε είναι ο ίδιος είτε έχει ανατεθεί αλλού

    private Integer status;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime requestedTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime acceptedTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime completedTime;

    private String conclusion;

    private String callee;

    private List<String> subs;

    private double commsN;

    //private List<AedEventComment> comments;
}
