package com.droukos.aedservice.service.aed_problem;

import com.droukos.aedservice.config.redis.RedisConfigProperties;
import com.droukos.aedservice.environment.dto.client.aed_problems.AedDeviceProblemDtoCreate;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import com.droukos.aedservice.repo.AedDeviceRepository;
import com.droukos.aedservice.repo.AedProblemsRepository;
import com.droukos.aedservice.service.validator.aed_problems.AedProblemsCreationValidator;
import com.droukos.aedservice.util.ValidatorUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import static com.droukos.aedservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@AllArgsConstructor
public class AedProblemsCreation {
    private final AedProblemsRepository aedProblemsRepository;
    private final AedDeviceRepository aedDeviceRepository;
    private final RedisConfigProperties redisConfigProperties;
    private final ReactiveRedisTemplate<String, AedProblems> reactiveRedisTemplateAedProblems;

    public void validateProblems(AedDeviceProblemDtoCreate aedProblemsDtoCreate) {
        ValidatorUtil.validate(aedProblemsDtoCreate, new AedProblemsCreationValidator());
    }

    public Mono<AedDevice> getDeviceById(AedDeviceProblemDtoCreate dtoCreate) {
        return aedDeviceRepository.getAedDeviceById(dtoCreate.getAedDeviceId())
                .flatMap(aedDevice -> aedDevice.getId() == null
                        ? Mono.error(badRequest("Aed Device id does not exist"))
                        : Mono.just(aedDevice));
    }



    public Mono<AedProblems> saveAedProblem(AedProblems problems) {
        return aedProblemsRepository.save(problems);
    }

    public Mono<Long> publishProblemOnRedisChannel(AedProblems aedProblem) {
        return this.reactiveRedisTemplateAedProblems.convertAndSend(redisConfigProperties.getAedProblemLiveChannel(), aedProblem);
    }
}
