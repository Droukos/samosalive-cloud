package com.droukos.aedservice.service.aed_problem;

import com.droukos.aedservice.environment.dto.client.aed_problems.AedProblemsDtoCreate;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import com.droukos.aedservice.repo.AedProblemsRepository;
import com.droukos.aedservice.service.validator.aed_problems.AedProblemsCreationValidator;
import com.droukos.aedservice.util.ValidatorUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.droukos.aedservice.model.factories.aed_problems.AedProblemsFactoryCreate.problemsCreate;

@Service
@RequiredArgsConstructor
public class AedProblemsCreation {
    @NonNull private final AedProblemsRepository aedProblemsRepository;

    public Mono<AedProblems> createAedProblems (AedProblemsDtoCreate aedProblemsDtoCreate){
        return Mono.just(problemsCreate (aedProblemsDtoCreate) );
    }
    public void validateProblems (AedProblemsDtoCreate aedProblemsDtoCreate) {
        ValidatorUtil.validate(aedProblemsDtoCreate, new AedProblemsCreationValidator());
    }

    public Mono<Boolean> saveAedProblem(AedProblems problems){
        return aedProblemsRepository.save(problems).then(Mono.just(true));
    }
}
