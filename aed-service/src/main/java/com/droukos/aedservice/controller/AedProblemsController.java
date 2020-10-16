package com.droukos.aedservice.controller;

import com.droukos.aedservice.environment.dto.client.aed_problems.AedProblemsDtoCreate;
import com.droukos.aedservice.environment.dto.client.aed_problems.AedProblemsDtoSearch;
import com.droukos.aedservice.environment.dto.server.aed.aedProblem.RequestedPreviewAedProblems;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import com.droukos.aedservice.service.aed_problem.AedProblemsCreation;
import com.droukos.aedservice.service.aed_problem.AedProblemsInfo;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class AedProblemsController {

    private final AedProblemsCreation aedProblemsCreation;
    private final AedProblemsInfo aedProblemsInfo;

    @MessageMapping("aed.problems.post")
    public Mono<Boolean> createProblems(AedProblemsDtoCreate aedProblemsDtoCreate){
        return Mono.just(aedProblemsDtoCreate)
                .doOnNext(aedProblemsCreation::validateProblems)
                .flatMap(aedProblemsCreation::createAedProblems)
                .flatMap((aedProblemsCreation::saveAedProblem));
    }

    @MessageMapping("aed.problems.get")
    public Flux<RequestedPreviewAedProblems> findProblems(AedProblemsDtoSearch aedProblemsDtoSearch) {
        return Flux.just(aedProblemsDtoSearch)
                .doOnNext(aedProblemsInfo::validateTitle)
                .flatMap(aedProblemsInfo::findProblemsByTitle)
                .flatMap(aedProblemsInfo::fetchProblemsByTitle);
    }
}
