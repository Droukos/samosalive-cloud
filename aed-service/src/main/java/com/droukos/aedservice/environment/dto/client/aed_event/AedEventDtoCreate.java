package com.droukos.aedservice.environment.dto.client.aed_event;

import lombok.*;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;
import java.util.Date;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class AedEventDtoCreate {
    private String userid;
    private String username;
    private Integer occurrenceType;
    private double mapX;
    private double mapY;
    private String address;
    private String comment;
}
