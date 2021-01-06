package com.droukos.aedservice.service.aed_problem;

import com.droukos.aedservice.config.redis.RedisConfigProperties;
import com.droukos.aedservice.environment.dto.client.aed_problems.AedProblemsDtoClose;
import com.droukos.aedservice.environment.dto.client.aed_problems.AedProblemsDtoSearch;
import com.droukos.aedservice.environment.dto.server.aed.aedProblem.RequestedAedProblems;
import com.droukos.aedservice.environment.dto.server.aed.aedProblem.RequestedPreviewAedProblems;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import com.droukos.aedservice.repo.AedDeviceRepository;
import com.droukos.aedservice.repo.AedProblemsRepository;
import com.droukos.aedservice.service.validator.aed_problems.AedProblemsTitleValidator;
import com.droukos.aedservice.util.ValidatorUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import static com.droukos.aedservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@AllArgsConstructor
public class AedProblemsInfo {
    private final AedProblemsRepository aedProblemsRepository;
    private final AedDeviceRepository aedDeviceRepository;
    private final RedisConfigProperties redisConfigProperties;
    private final ReactiveRedisTemplate<String, AedProblems> reactiveRedisTemplateAedProblems;

    public void validateTitle (AedProblemsDtoSearch aedProblemsDtoSearch){
        ValidatorUtil.validate(aedProblemsDtoSearch, new AedProblemsTitleValidator());
    }

    public Flux<AedProblems> findProblemsByTitle(AedProblemsDtoSearch aedProblemsDtoSearch) {
        return aedProblemsRepository.findAllByTitleContaining(aedProblemsDtoSearch.getTitle());
    }
    public Mono<RequestedPreviewAedProblems> fetchProblemsByTitle(AedProblems aedProblems){
        return Mono.just(RequestedPreviewAedProblems.build(aedProblems));
    }

    public Mono<AedProblems> findProblemsId(String id) {
        return aedProblemsRepository.findById(id)
                .defaultIfEmpty(new AedProblems())
                .flatMap(aedProblems -> aedProblems.getId() == null
                        ? Mono.error(badRequest("Event not found"))
                        : Mono.just(aedProblems));
    }

    public Mono<Tuple3<AedProblems, AedProblemsDtoClose, AedDevice>> fetchDeviceFromProblemModel(Tuple2<AedProblems, AedProblemsDtoClose> tuple2) {
        return Mono.zip(
                Mono.just(tuple2.getT1()),
                Mono.just(tuple2.getT2()),
                aedDeviceRepository.getAedDeviceById(tuple2.getT1().getAedDevId())
        );
    }

    public Mono<AedProblems> saveAedProblems(AedProblems aedProblems){
        return aedProblemsRepository.save(aedProblems);
    }

    public Flux<AedProblems> fetchPublishedAedProblems() {
        return this.reactiveRedisTemplateAedProblems
                .listenToChannel(redisConfigProperties.getAedProblemLiveChannel())
                .flatMap(reactiveSubMsg -> Mono.just(reactiveSubMsg.getMessage()));
    }
}
