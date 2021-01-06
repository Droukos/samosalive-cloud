package com.droukos.aedservice.environment.dto.client.osm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Waypoints {
    private double lat;
    private double lon;

    public static Waypoints build(double lat, double lon) {
        return new Waypoints(lat, lon);
    }
}
