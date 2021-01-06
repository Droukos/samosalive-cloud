package com.droukos.aedservice.service.osrm;

import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceAreaLookWithRoute;
import com.droukos.aedservice.environment.dto.client.osm.OsrmWaypoint;
import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceInfoPreviewDto;
import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDevicePreviewWithRouteDto;
import com.droukos.aedservice.environment.dto.server.aed.osrm.RouteInfoDto;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@AllArgsConstructor
public class OsrmService {
    private final WebClient clientOsrm = WebClient.create("http://localhost:5000");

    //http://router.project-osrm.org/route/v1/driving/13.388860,52.517037;13.397634,52.529407;13.428555,52.523219?steps=true&overview=full
    public Mono<RouteInfoDto> fetchWaypointRoute(OsrmWaypoint osrmWaypoint) {
        String waypoints = StringUtils.chop(osrmWaypoint.getWaypoints()
                .stream()
                .map(e -> e.getLon() + "," + e.getLat() + ";")
                .reduce("", String::concat));

        return clientOsrm.get()
                .uri("/route/v1/driving/{waypoints}?steps=true&overview=full", waypoints)
                .retrieve()
                .bodyToMono(String.class)
                .zipWith(Mono.just(osrmWaypoint))
                .flatMap(RouteInfoDto::buildMono);
    }

    public Mono<AedDevicePreviewWithRouteDto> fetchRescuerDeviceEventRoute(AedDeviceInfoPreviewDto aedDeviceInfoPreviewDto, AedDeviceAreaLookWithRoute dto) {

        return fetchWaypointRoute(OsrmWaypoint.build(aedDeviceInfoPreviewDto, dto))
                .zipWith(Mono.just(aedDeviceInfoPreviewDto))
                .flatMap(AedDevicePreviewWithRouteDto::buildMono);
    }
}
