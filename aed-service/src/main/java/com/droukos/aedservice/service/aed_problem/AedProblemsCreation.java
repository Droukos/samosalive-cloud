package com.droukos.aedservice.service.aed_problem;

import com.droukos.aedservice.environment.constants.StatusCodes;
import com.droukos.aedservice.environment.dto.client.aed_problems.AedProblemsDtoCreate;
import com.droukos.aedservice.environment.dto.server.ApiResponse;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import com.droukos.aedservice.repo.AedProblemsRepository;
import com.droukos.aedservice.service.validator.aed_event.AedCreationValidator;
import com.droukos.aedservice.service.validator.aed_problems.AedProblemsCreationValidator;
import com.droukos.aedservice.util.ValidatorUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static com.droukos.aedservice.model.factories.AedProblemsFactoryCreate.problemsCreate;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@RequiredArgsConstructor
public class AedProblemsCreation {
    @NonNull private final AedProblemsRepository aedProblemsRepository;

    public Mono<AedProblems> createAedProblems (AedProblemsDtoCreate aedProblemsDtoCreate){
        return Mono.just(problemsCreate (aedProblemsDtoCreate) );
    }
    public void validateProblems (AedProblemsDtoCreate aedProblemsDtoCreate) {
        ValidatorUtil.validate(aedProblemsDtoCreate, new AedCreationValidator());
    }

    public Mono<ServerResponse> saveAedProblem(AedProblems problems){
        Function<AedProblems, Mono<ServerResponse>> result = savedProblems -> ok().contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(new ApiResponse(StatusCodes.OK, "Problem created", "problem.created")));

        return aedProblemsRepository.save(problems).flatMap(result);
    }
}
