package com.droukos.aedservice.environment.dto.client.aed_device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AedDeviceAreaLookWithRoute {
    private String eventId;
    private double eventLat;
    private double eventLng;
    private int distance;
    private double rescuerLat;
    private double rescuerLng;
}
