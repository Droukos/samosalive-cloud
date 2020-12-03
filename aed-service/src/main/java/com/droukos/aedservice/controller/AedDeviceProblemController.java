package com.droukos.aedservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class AedDeviceProblemController {

    public Mono<Boolean> createAedDeviceProblem() {

        return Mono.empty();
    }

    public Mono<Boolean> updateAedDeviceProblemInfo() {

        return Mono.empty();
    }

    public Mono<Boolean> fixedAedDeviceProblem() {
        return Mono.empty();
    }

}
