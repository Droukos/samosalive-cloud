package com.droukos.aedservice.service.aed_problem;

import com.droukos.aedservice.config.redis.RedisConfigProperties;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AedProblemsChannel {
    private final RedisConfigProperties redisConfigProperties;
    private final ReactiveRedisTemplate<String, AedProblems> reactiveRedisTemplateAedProblems;

    public Mono<AedProblems> publishProblemOnRedisChannel(AedProblems aedProblems) {
        return this.reactiveRedisTemplateAedProblems.convertAndSend(redisConfigProperties.getAedProblemLiveChannel(), aedProblems)
                .then(Mono.just(aedProblems));
    }

    public Mono<AedProblems> publishEventOnRedisSingleChannel(AedProblems aedProblems) {
        return this.reactiveRedisTemplateAedProblems
                .convertAndSend(redisConfigProperties.getAedProblemSingleChannelPrefix() + aedProblems.getId(), aedProblems)
                .then(Mono.just(aedProblems));
    }
}
