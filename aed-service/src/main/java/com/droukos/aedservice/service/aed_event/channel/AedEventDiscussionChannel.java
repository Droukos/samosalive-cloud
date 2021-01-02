package com.droukos.aedservice.service.aed_event.channel;

import com.droukos.aedservice.config.redis.RedisConfigProperties;
import com.droukos.aedservice.environment.dto.RequesterAccessTokenData;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventCommentDto;
import com.droukos.aedservice.environment.dto.server.user.RequestedPreviewRescuer;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.model.aed_event.AedEventComment;
import com.droukos.aedservice.repo.AedEventDiscussionRepository;
import com.droukos.aedservice.repo.AedEventRepository;
import com.droukos.aedservice.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import static com.droukos.aedservice.model.factories.aed_event.AedEventFactoryComments.addFieldAllCommsNum;
import static com.droukos.aedservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@AllArgsConstructor
public class AedEventDiscussionChannel {
    private final RedisConfigProperties redisConfigProperties;
    private final AedEventRepository aedEventRepository;
    private final AedEventDiscussionRepository aedEventDiscussionRepository;
    private final ReactiveRedisTemplate<String, AedEventComment> reactiveRedisTemplateAedComments;

    public Mono<Tuple3<AedEvent, AedEventCommentDto, RequesterAccessTokenData>> validateUserBelongsToChannel
            (Tuple3<AedEvent, AedEventCommentDto, SecurityContext> tuple3) {

        RequesterAccessTokenData requesterData = SecurityUtil.getRequesterData(tuple3.getT3());
        return tuple3.getT1().getSubs().stream().anyMatch(username -> requesterData.getUsername().equals(username))
                ? Mono.zip(Mono.just(tuple3.getT1()), Mono.just(tuple3.getT2()), Mono.just(requesterData))
                : Mono.error(badRequest("User does not belong in subs for channel"));
    }

    public Mono<Tuple3<AedEvent, AedEventCommentDto, RequesterAccessTokenData>> saveEvent(Tuple3<AedEvent, AedEventCommentDto, RequesterAccessTokenData> tuple3) {
        return aedEventRepository.save(tuple3.getT1())
                .then(Mono.just(tuple3));
    }
    public Mono<Tuple2<AedEvent, AedEventComment>> saveOnEventDiscussion(Tuple3<AedEvent, AedEventCommentDto, RequesterAccessTokenData> tuple3) {

        AedEventComment newComment = AedEventComment.build(tuple3);
        return aedEventDiscussionRepository.save(newComment)
                .then(Mono.zip(Mono.just(tuple3.getT1()), Mono.just(newComment)));
    }

    public Mono<AedEventCommentDto> validateComment(AedEventCommentDto aedEventCommentDto) {
        return aedEventCommentDto.getComment().length()> 1200
                ? Mono.error(badRequest("Max comment size reached"))
                : Mono.just(aedEventCommentDto);
    }

    public Mono<AedEventComment> publishOnEventDiscussion(Tuple2<AedEvent, AedEventComment> tuple2) {

        AedEventComment comment = addFieldAllCommsNum(tuple2.getT2());
        return this.reactiveRedisTemplateAedComments
                .convertAndSend(redisConfigProperties.getAedEventSingleChannelPrefix()
                        + tuple2.getT1().getId()+ redisConfigProperties.getDiscussionPostfix(), comment)
                .then(Mono.just(comment));
    }

    public Flux<AedEventComment> fetchPublishedCommentsForSingleEvent(String channel) {
        return this.reactiveRedisTemplateAedComments
                .listenToChannel(redisConfigProperties.getAedEventSingleChannelPrefix() +
                        channel + redisConfigProperties.getDiscussionPostfix())
                .flatMap(reactiveSubMsg -> Mono.just(reactiveSubMsg.getMessage()));
    }
}
