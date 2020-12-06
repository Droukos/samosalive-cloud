package com.droukos.aedservice.service.aed_event;

import com.droukos.aedservice.config.redis.RedisConfigProperties;
import com.droukos.aedservice.environment.constants.authorities.Roles;
import com.droukos.aedservice.environment.dto.RequesterAccessTokenData;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import static com.droukos.aedservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@AllArgsConstructor
public class AedEventChannel {
    private final RedisConfigProperties redisConfigProperties;
    private final ReactiveRedisTemplate<String, AedEvent> reactiveRedisTemplate;

    public Mono<AedEvent> validateListenerForEvent(Tuple2<AedEvent, SecurityContext> tuple2) {
        RequesterAccessTokenData requesterData = SecurityUtil.getRequesterData(tuple2.getT2());
        AedEvent aedEvent = tuple2.getT1();
        return (requesterData.getUsername().toLowerCase().equals(aedEvent.getUsername())
                || requesterData.getUsername().toLowerCase().equals(aedEvent.getRescuer())
                || requesterData.getRoles().stream().anyMatch(role -> role.equals(Roles.GENERAL_ADMIN)))
                ? Mono.just(aedEvent)
                : Mono.error(badRequest());
    }

    public Mono<Long> publishEventOnRedisChannel(String channel, AedEvent aedEvent) {
        return this.reactiveRedisTemplate.convertAndSend(
                redisConfigProperties
                        .getAedEventSingleChannelPrefix() + channel, aedEvent);
    }

    public Mono<Long> publishEventOnRedisChannel(AedEvent aedEvent) {
        return this.reactiveRedisTemplate.convertAndSend(redisConfigProperties.getAedEventLiveChannel(), aedEvent);
    }

    public Flux<AedEvent> fetchPublishedAedEvents() {
        return this.reactiveRedisTemplate
                .listenToChannel(redisConfigProperties.getAedEventLiveChannel())
                .flatMap(reactiveSubMsg -> Mono.just(reactiveSubMsg.getMessage()));
    }

    public Flux<AedEvent> fetchPublishedAedEventsFromSingle(AedEvent aedEvent) {
        return this.fetchPublishedAedEventsFromSingle(aedEvent.getId());
    }

    public Flux<AedEvent> fetchPublishedAedEventsFromSingle(String channel) {
        return this.reactiveRedisTemplate
                .listenToChannel(redisConfigProperties.getAedEventSingleChannelPrefix() + channel)
                .flatMap(reactiveSubMsg -> Mono.just(reactiveSubMsg.getMessage()));
    }
}
