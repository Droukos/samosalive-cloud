package com.droukos.osmservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class OsmController {
    private final WebClient clientOsm = WebClient.create("http://localhost:7070");
    private final WebClient clientOsrm = WebClient.create("http://localhost:5000");

    @MessageMapping("aed.osm.search")
    public Mono<ClientResponse> searchOnOsm() {
        return clientOsm.get()
                .uri("/search")
                .exchange();
    }

    @MessageMapping("aed.osm.reverse")
    public Mono<ClientResponse> reverseOnOsm() {
        return clientOsm.get()
                .uri("/reverse?format=json&lat=37.992205688896&lon=23.79810333252&zoom=10")
                .exchange();
    }
}
