package com.droukos.aedservice.service.aed_event.channel;

import com.droukos.aedservice.config.redis.RedisConfigProperties;
import com.droukos.aedservice.environment.dto.server.user.RequestedPreviewRescuer;
import com.droukos.aedservice.model.aed_device.AedDevice;
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
public class AedEventRescuerChannel {
    private final RedisConfigProperties redisConfigProperties;
    private final ReactiveRedisTemplate<String, RequestedPreviewRescuer> reactiveRedisTemplateRescuer;


    public Mono<Tuple2<AedEvent, AedDevice>> publishRescuerOnRedisEventSubChannel(Tuple3<AedEvent, AedDevice, UserRes> tuple3) {
        return this.reactiveRedisTemplateRescuer
                .convertAndSend(redisConfigProperties.getAedEventSingleChannelPrefix()
                        + tuple3.getT1().getId() + redisConfigProperties.getRescuerPostfix(),
                        RequestedPreviewRescuer.build(tuple3.getT3()))
                .then(Mono.zip(Mono.just(tuple3.getT1()), Mono.just(tuple3.getT2())));
    }

    public Flux<RequestedPreviewRescuer> fetchPublishedRescuerFromSingleEvent(String channel) {
        return this.reactiveRedisTemplateRescuer
                .listenToChannel(redisConfigProperties.getAedEventSingleChannelPrefix() +
                        channel + redisConfigProperties.getRescuerPostfix())
                .flatMap(reactiveSubMsg -> Mono.just(reactiveSubMsg.getMessage()));
    }
}
