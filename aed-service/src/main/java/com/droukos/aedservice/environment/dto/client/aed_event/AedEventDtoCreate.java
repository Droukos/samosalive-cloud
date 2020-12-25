package com.droukos.aedservice.environment.dto.client.aed_event;

import lombok.*;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;
import java.util.Date;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class AedEventDtoCreate {
    private String username;
    private Integer occurrenceType;
    private double mapLat;
    private double mapLon;
    private String callee;
    private String phone;
    private String address;
    private String comment;
}
