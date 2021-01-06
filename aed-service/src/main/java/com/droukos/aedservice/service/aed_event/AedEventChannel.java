package com.droukos.aedservice.service.aed_event;

import com.droukos.aedservice.config.AppConfig;
import com.droukos.aedservice.config.redis.RedisConfigProperties;
import com.droukos.aedservice.environment.constants.authorities.Roles;
import com.droukos.aedservice.environment.dto.RequesterAccessTokenData;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDiscussionDto;
import com.droukos.aedservice.environment.enums.AedEventStatus;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.model.aed_event.AedEventComment;
import com.droukos.aedservice.model.factories.user.UserAedEventChannelSub;
import com.droukos.aedservice.model.user.UserRes;
import com.droukos.aedservice.repo.AedEventRepository;
import com.droukos.aedservice.repo.UserRepository;
import com.droukos.aedservice.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import static com.droukos.aedservice.model.factories.aed_event.AedEventFactorySubListeners.buildAedEventWithListener;
import static com.droukos.aedservice.model.factories.user.UserAedEventChannelSub.buildUserByRemovingSubMono;
import static com.droukos.aedservice.util.factories.HttpExceptionFactory.badRequest;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@AllArgsConstructor
public class AedEventChannel {
    private final RedisConfigProperties redisConfigProperties;
    private final AppConfig appConfig;
    private final UserRepository userRepository;
    private final AedEventRepository aedEventRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final ReactiveRedisTemplate<String, AedEvent> reactiveRedisTemplate;

    public Mono<Tuple2<AedEvent, String>> validateListenerForEvent(Tuple2<AedEvent, SecurityContext> tuple2) {
        RequesterAccessTokenData requesterData = SecurityUtil.getRequesterData(tuple2.getT2());
        AedEvent aedEvent = tuple2.getT1();
        return aedEvent.getStatus() != AedEventStatus.COMPLETED.getStatus()
                || requesterData.getUsername().toLowerCase().equals(aedEvent.getUsername())
                || requesterData.getUsername().toLowerCase().equals(aedEvent.getRescuer())
                || requesterData.getRoles().stream().anyMatch(role ->
                role.equals(Roles.GENERAL_ADMIN) || role.equals(Roles.RESCUER))

                ? Mono.zip(Mono.just(aedEvent), Mono.just(requesterData.getUsername()))
                : Mono.error(badRequest());
    }

    public Mono<String> validateListenerForEventPostfix(Tuple2<AedEvent, SecurityContext> tuple2) {
        return this.validateListenerForEvent(tuple2)
                .then(Mono.just(tuple2.getT1().getId()));
    }

    public Mono<AedEvent> validateUserForDiscussion(Tuple2<AedEvent, SecurityContext> tuple2) {
        RequesterAccessTokenData requesterData = SecurityUtil.getRequesterData(tuple2.getT2());
        AedEvent aedEvent = tuple2.getT1();
        return aedEvent.getUsername().equals(requesterData.getUsername().toLowerCase())
                ||
                requesterData.getRoles().stream().anyMatch(role -> role.equals(Roles.GENERAL_ADMIN) || role.equals(Roles.RESCUER))
                ? Mono.just(aedEvent)
                : Mono.error(badRequest());
    }

    public Flux<AedEventComment> fetchEventComments(AedEventDiscussionDto dto) {
        return reactiveMongoTemplate.aggregate(
                newAggregation(
                        match(Criteria.where("eventId").is(dto.getEventId())),
                        skip(dto.getPageOffset() * appConfig.getCommentsOffset()),
                        sort(Sort.Direction.ASC, "posted")), AedEventComment.class, AedEventComment.class
        );
    }


    public Mono<Tuple3<AedEvent, UserRes, String>> checkAndInsertUserSubOnDb(Tuple2<AedEvent, String> tuple2) {
        AedEvent aedEvent = tuple2.getT1();
        String aedEventChannel = redisConfigProperties.getAedEventSingleChannelPrefix() + aedEvent.getId();
        return userRepository.findFirstByUser(tuple2.getT2())
                .flatMap(userRes ->
                        userRes.getChannelSubs() == null
                                || userRes.getChannelSubs().getAedEvSubs() == null
                                || userRes.getChannelSubs().getAedEvSubs().keySet().stream()
                                .noneMatch(channelId -> channelId.equals(aedEventChannel))
                                ? UserAedEventChannelSub.buildUserNewEventSubListMono(userRes, aedEventChannel)
                                : UserAedEventChannelSub.buildUserWithoutSysMono(userRes))
                .flatMap(userRes -> userRes.getSys() != null
                        ? userRepository.save(userRes)
                        : Mono.just(userRes))
                .flatMap(user ->
                        Mono.zip(
                                Mono.just(tuple2.getT1()),
                                Mono.just(user),
                                Mono.just(tuple2.getT2())));
    }


    public Mono<AedEvent> checkAndInsertAedEventSubOnDb(Tuple2<AedEvent, String> tuple2) {
        AedEvent aedEvent = tuple2.getT1();
        String username = tuple2.getT2();
        return aedEvent.getSubs() == null
                || aedEvent.getSubs().stream().noneMatch(sub -> sub.equals(username))
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
