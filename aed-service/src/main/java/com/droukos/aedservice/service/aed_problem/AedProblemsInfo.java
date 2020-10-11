package com.droukos.aedservice.service.aed_problem;

import com.droukos.aedservice.environment.dto.server.aed.aedProblem.RequestedPreviewAedProblems;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import com.droukos.aedservice.repo.AedProblemsRepository;
import com.droukos.aedservice.environment.dto.server.aed.aedProblem.RequestedPreviewAedProblems;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import com.droukos.aedservice.repo.AedProblemsRepository;
import com.droukos.aedservice.service.validator.aed_problems.AedProblemsTitleValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static com.droukos.aedservice.util.ValidatorUtil.validate;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@RequiredArgsConstructor
public class AedProblemsInfo {
    @NonNull
    private final AedProblemsRepository aedProblemsRepository;

    //public Mono<AedProblems> validateTitle (AedProblems problems){
    //    //Consumer<Event> prepareUpdate = updateEvent -> {};
//
    //    validate(problems, new AedProblemsTitleValidator());
    //    //prepareUpdate.accept(updateEvent);
    //    return Mono.just(problems);
    //}

    public Mono<ServerResponse> fetchProblemsByTitle(AedProblems problems) {
        Function<String, Flux<RequestedPreviewAedProblems>> fetchProblemsByTitle = title ->
                aedProblemsRepository.findAllByProblemTitleContaining(title)
                        .flatMap(fetchedProblem -> Mono.just(RequestedPreviewAedProblems.build(fetchedProblem)));

        Function<Flux<RequestedPreviewAedProblems>, Mono<ServerResponse>> result = requested_previewAedProblem ->
                ok().body(requested_previewAedProblem, RequestedPreviewAedProblems.class);

        return result.apply(fetchProblemsByTitle.apply(problems.getProblemTitle()));
    }
}
