package com.droukos.aedservice.service.aed_event.channel;

import com.droukos.aedservice.config.redis.RedisConfigProperties;
import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceInfoPreviewDto;
import com.droukos.aedservice.environment.dto.server.user.RequestedPreviewRescuer;
import com.droukos.aedservice.environment.dto.server.user.RequestedPreviewUser;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.model.user.UserRes;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

@Service
@AllArgsConstructor
public class AedEventUserChannel {
    private final RedisConfigProperties redisConfigProperties;
    private final ReactiveRedisTemplate<String, RequestedPreviewUser> reactiveRedisTemplateUser;

    public Flux<RequestedPreviewUser> fetchPublishedUsersForSingleEvent(String channel) {
        return this.reactiveRedisTemplateUser
                .listenToChannel(redisConfigProperties.getAedEventSingleChannelPrefix() +
                        channel + redisConfigProperties.getUserPostfix())
                .flatMap(reactiveSubMsg -> Mono.just(reactiveSubMsg.getMessage()));
    }

    public Mono<Tuple2<AedEvent, String>> publishUserForSingleEvent(Tuple3<AedEvent, UserRes, String> tuple3) {
        return this.reactiveRedisTemplateUser
                .convertAndSend(redisConfigProperties.getAedEventSingleChannelPrefix()
                                + tuple3.getT1().getId() + redisConfigProperties.getUserPostfix()
                        , RequestedPreviewUser.build(tuple3.getT2()))
                .then(Mono.zip(Mono.just(tuple3.getT1()), Mono.just(tuple3.getT3())));
    }
}
