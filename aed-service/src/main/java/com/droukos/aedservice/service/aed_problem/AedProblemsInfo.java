package com.droukos.aedservice.service.aed_problem;

import com.droukos.aedservice.environment.dto.client.aed_problems.AedProblemsDtoSearch;
import com.droukos.aedservice.environment.dto.server.aed.aedProblem.RequestedPreviewAedProblems;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import com.droukos.aedservice.repo.AedProblemsRepository;
import com.droukos.aedservice.service.validator.aed_problems.AedProblemsTitleValidator;
import com.droukos.aedservice.util.ValidatorUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AedProblemsInfo {
    @NonNull private final AedProblemsRepository aedProblemsRepository;

    public void validateTitle (AedProblemsDtoSearch aedProblemsDtoSearch){
        ValidatorUtil.validate(aedProblemsDtoSearch, new AedProblemsTitleValidator());
    }

    public Flux<AedProblems> findProblemsByTitle(AedProblemsDtoSearch aedProblemsDtoSearch) {
        return aedProblemsRepository.findAllByProblemTitleContaining(aedProblemsDtoSearch.getTitle());
    }
    public Mono<RequestedPreviewAedProblems> fetchProblemsByTitle(AedProblems aedProblems){
        return Mono.just(RequestedPreviewAedProblems.build(aedProblems));
    }
}
