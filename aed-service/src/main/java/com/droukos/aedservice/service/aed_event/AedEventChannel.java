package com.droukos.aedservice.service.aed_event;

import com.droukos.aedservice.config.redis.RedisConfigProperties;
import com.droukos.aedservice.environment.constants.authorities.Roles;
import com.droukos.aedservice.environment.dto.RequesterAccessTokenData;
import com.droukos.aedservice.environment.enums.AedEventStatus;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.model.factories.user.UserAedEventChannelSub;
import com.droukos.aedservice.model.user.UserRes;
import com.droukos.aedservice.repo.AedEventRepository;
import com.droukos.aedservice.repo.UserRepository;
import com.droukos.aedservice.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import static com.droukos.aedservice.model.factories.aed_event.AedEventFactorySubListeners.buildAedEventWithListener;
import static com.droukos.aedservice.model.factories.user.UserAedEventChannelSub.buildUserByRemovingSubMono;
import static com.droukos.aedservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@AllArgsConstructor
public class AedEventChannel {
    private final RedisConfigProperties redisConfigProperties;
    private final UserRepository userRepository;
    private final AedEventRepository aedEventRepository;
    private final ReactiveRedisTemplate<String, AedEvent> reactiveRedisTemplate;

    public Mono<Tuple2<AedEvent, String>> validateListenerForEvent(Tuple2<AedEvent, SecurityContext> tuple2) {
        RequesterAccessTokenData requesterData = SecurityUtil.getRequesterData(tuple2.getT2());
        AedEvent aedEvent = tuple2.getT1();
        return aedEvent.getStatus() != AedEventStatus.COMPLETED.getStatus()
                || requesterData.getUsername().toLowerCase().equals(aedEvent.getUsername())
                || requesterData.getUsername().toLowerCase().equals(aedEvent.getRescuer())
                || requesterData.getRoles().stream().anyMatch(role -> role.equals(Roles.GENERAL_ADMIN))
                ? Mono.zip(Mono.just(aedEvent), Mono.just(requesterData.getUsername()))
                : Mono.error(badRequest());
    }

    public Mono<Tuple2<AedEvent, String>> checkAndInsertUserSubOnDb(Tuple2<AedEvent, String> tuple2) {
        AedEvent aedEvent = tuple2.getT1();
        String aedEventChannel = redisConfigProperties.getAedEventSingleChannelPrefix() + aedEvent.getId();
        return userRepository.findFirstByUser(tuple2.getT2())
                .flatMap(userRes ->
                        userRes.getChannelSubs() == null
                                || userRes.getChannelSubs().getAedEvSubs() == null
                                || userRes.getChannelSubs().getAedEvSubs().stream()
                                .noneMatch(channel -> channel.equals(aedEventChannel))
                                ? UserAedEventChannelSub.buildUserNewEventSubListMono(userRes, aedEvent.getId())
                                : Mono.empty())
                .defaultIfEmpty(new UserRes())
                .flatMap(userRes -> userRes.getUser() != null
                        ? userRepository.save(userRes)
                        : Mono.just(userRes))
                .then(Mono.just(tuple2));
    }

    public Mono<AedEvent> checkAndInsertAedEventSubOnDb(Tuple2<AedEvent, String> tuple2) {
        AedEvent aedEvent = tuple2.getT1();
        String username = tuple2.getT2();
        return aedEvent.getSubs().stream().noneMatch(sub -> sub.equals(username))
                ? aedEventRepository.save(buildAedEventWithListener(tuple2))
                : Mono.just(aedEvent);
    }


    public Mono<AedEvent> removeUsersChannelSubFromDb(AedEvent aedEvent) {
        String aedEventChannel = redisConfigProperties.getAedEventSingleChannelPrefix() + aedEvent.getId();

        return userRepository.findAllByUserIn(aedEvent.getSubs())
                .flatMap(user -> buildUserByRemovingSubMono(user, aedEventChannel))
                .collectList()
                .flatMapMany(userRepository::saveAll)
                .then(Mono.just(aedEvent));
    }

    public Mono<AedEvent> publishEventOnRedisSingleChannel(AedEvent aedEvent) {
        return this.reactiveRedisTemplate
                .convertAndSend(redisConfigProperties.getAedEventSingleChannelPrefix() + aedEvent.getId(), aedEvent)
                .then(Mono.just(aedEvent));
    }

    public Mono<AedEvent> publishEventOnRedisChannel(AedEvent aedEvent) {
        return this.reactiveRedisTemplate.convertAndSend(redisConfigProperties.getAedEventLiveChannel(), aedEvent)
                .then(Mono.just(aedEvent));
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
