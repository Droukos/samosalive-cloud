package com.droukos.aedservice.controller;

import com.droukos.aedservice.environment.dto.client.osm.OsrmWaypoint;
import com.droukos.aedservice.environment.dto.client.osm.OsrmWaypoints;
import com.droukos.aedservice.environment.dto.client.osm.SearchAddressDto;
import com.droukos.aedservice.environment.dto.client.osm.SearchLatLonDto;
import com.droukos.aedservice.environment.dto.server.aed.osrm.RouteInfoDto;
import com.droukos.aedservice.service.osrm.OsrmService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Controller
@AllArgsConstructor
public class OsmController {
    //@Value("${samosalive.osm}")
    //private final String osmHost;
    private final WebClient clientOsm = WebClient.create("http://localhost:7070");
    private final WebClient clientOsrm = WebClient.create("http://localhost:5000");
    private final OsrmService osrmService;

    @MessageMapping("aed.osm.search")
    public Mono<String> searchOnOsm(SearchAddressDto searchAddressDto) {
        return clientOsm.get()
                .uri("/search?format=json&q={address}", searchAddressDto.getAddress())
                .retrieve()
                .bodyToMono(String.class);
    }

    @MessageMapping("aed.osm.reverse")
    public Mono<String> reverseOnOsm(SearchLatLonDto searchLatLonDto) {
        return clientOsm.get()
                .uri("/reverse?format=json&lat={lat}&lon={lon}", searchLatLonDto.getLat(), searchLatLonDto.getLon())
                .retrieve()
                .bodyToMono(String.class);
    }

    @MessageMapping("aed.osrm.search")
    public Mono<RouteInfoDto> osrmSearch(OsrmWaypoint osrmWaypoint) {
        return osrmService.fetchWaypointRoute(osrmWaypoint);
    }

    @MessageMapping("aed.osrm.search.resc_dev_ev")
    public Flux<RouteInfoDto> osrmSearchDevicesForEv(OsrmWaypoints osrmWaypoints) {

        return Flux.just(osrmWaypoints.getOsrmWaypoints())
                .flatMap(osrmService::fetchWaypointRoute);
    }
}
