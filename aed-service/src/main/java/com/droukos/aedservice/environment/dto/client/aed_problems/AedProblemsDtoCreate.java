package com.droukos.aedservice.environment.dto.client.aed_problems;

import lombok.*;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class AedProblemsDtoCreate {
        private String username;
        private String title;
        private String body;
        private double y;
        private double x;
        private String address;

}
