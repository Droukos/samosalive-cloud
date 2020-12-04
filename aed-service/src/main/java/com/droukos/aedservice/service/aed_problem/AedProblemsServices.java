package com.droukos.aedservice.service.aed_problem;

import com.droukos.aedservice.model.aed_problems.AedProblems;
import com.droukos.aedservice.repo.AedProblemsRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Getter
public class AedProblemsServices {
    @NonNull private final AedProblemsRepository aedProblemsRepository;

    public Flux<AedProblems> getProblemsByTitle(String title) {
        return aedProblemsRepository.findAllByTitleContaining(title);
    }

    //public Mono<Problems> validateInfomation (ServerRequest request){
    //    return request.bodyToMono(Problems.class)
    //            .defaultIfEmpty(new Problems())
    //            .flatMap(problems -> {
    //                ValidatorUtil.validate(problems, new AedProblemsCreationValidator());
    //                return Mono.just(problems);
    //            });
    //}
//
    //public Mono<Problems> validateTitle (ServerRequest request){
    //    return request.bodyToMono(Problems.class)
    //            .defaultIfEmpty(new Problems())
    //            .flatMap(problems -> {
    //                ValidatorUtil.validate(problems, new AedProblemsTitleValidator());
    //                return Mono.just(problems);
    //            });
    //}

    public Flux<AedProblems> getAllProblems() {
        return aedProblemsRepository.findAll();
    }

    public Mono<AedProblems> createProblemsDto(ServerRequest request){
        return request.bodyToMono(AedProblems.class).defaultIfEmpty(new AedProblems());
    }
}
