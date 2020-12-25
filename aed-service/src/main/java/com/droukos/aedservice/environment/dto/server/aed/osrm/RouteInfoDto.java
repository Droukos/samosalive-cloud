package com.droukos.aedservice.environment.dto.server.aed.osrm;

import com.droukos.aedservice.environment.dto.client.osm.OsrmWaypoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RouteInfoDto {
    private String aedEventId;
    private String aedDeviceId;
    private String routeInfo;

    public static Mono<RouteInfoDto> buildMono(Tuple2<String, OsrmWaypoint> tuple2) {
        return Mono.just(build(tuple2));
    }

    public static RouteInfoDto build(Tuple2<String, OsrmWaypoint> tuple2) {
        OsrmWaypoint osrmWaypoint = tuple2.getT2();
        return new RouteInfoDto(
                osrmWaypoint.getAedEventId(),
                osrmWaypoint.getAedDeviceId(),
                tuple2.getT1()
        );
    }
}
