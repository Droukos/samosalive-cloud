package com.droukos.userservice.service.user;

import com.droukos.userservice.config.redis.RedisConfigProperties;
import com.droukos.userservice.environment.dto.server.user.RequestedPreviewRescuer;
import com.droukos.userservice.environment.dto.server.user.RequestedPreviewUser;
import com.droukos.userservice.model.user.EventChannel;
import com.droukos.userservice.model.user.UserRes;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@AllArgsConstructor
public class EventChannelService {
    private final RedisConfigProperties redisConfigProperties;
    private final ReactiveRedisTemplate<String, RequestedPreviewUser> reactiveRedisTemplateUser;
    private final ReactiveRedisTemplate<String, RequestedPreviewRescuer> reactiveRedisTemplateRescuer;

    public Mono<UserRes> publishUserForSubbedEvents(UserRes user) {

        RequestedPreviewUser previewUser = RequestedPreviewUser.build(user);
        RequestedPreviewRescuer previewRescuer = RequestedPreviewRescuer.build(user);
        Map<String, EventChannel> eventChannelMap = user.getChannelSubs().getAedEvSubs();

        return eventChannelMap == null
                ? Mono.just(user)
                : Flux.fromStream(eventChannelMap.keySet().stream())
                .flatMap(key ->
                        eventChannelMap.get(key).isRescuer()
                            ? this.reactiveRedisTemplateRescuer.convertAndSend(key+ redisConfigProperties.getRescuerPostfix(), previewRescuer)
                            : this.reactiveRedisTemplateUser.convertAndSend(key + redisConfigProperties.getUserPostfix(), previewUser)
                )
                .then(Mono.just(user));
    }
}
