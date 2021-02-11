package com.droukos.aedservice.environment.dto.client.aed_problems;

import lombok.*;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class AedDeviceProblemDtoCreate {
        private String username;
        private Integer title;
        private String body;
        private String aedDeviceId;
}
