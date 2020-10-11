package com.droukos.aedservice.controller;

import com.droukos.aedservice.environment.dto.client.aed_problems.AedProblemsDtoCreate;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import com.droukos.aedservice.service.aed_event.AedEventCreation;
import com.droukos.aedservice.service.aed_problem.AedProblemsCreation;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class AedProblemsController {

    private final AedProblemsCreation aedProblemsCreation;

    @MessageMapping("aed.problems.createProblems")
    public Mono<AedProblems> createProblems(AedProblemsDtoCreate aedProblemsDtoCreate){
        return Mono.just(aedProblemsDtoCreate)
                .doOnNext(aedProblemsCreation::validateProblems)
                .flatMap(aedProblemsCreation::createAedProblems);
    }
}
