package com.droukos.aedservice.service.aed_event.channel;

import com.droukos.aedservice.config.redis.RedisConfigProperties;
import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceInfoPreviewDto;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.model.aed_event.AedEvent;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@AllArgsConstructor
public class AedEventDeviceChannel {
    private final RedisConfigProperties redisConfigProperties;
    private final ReactiveRedisTemplate<String, AedDeviceInfoPreviewDto> reactiveRedisTemplateAedDevices;

    public Mono<AedEvent> publishDeviceOnRedisEventSubChannel(Tuple2<AedEvent, AedDevice> tuple2) {
        return this.reactiveRedisTemplateAedDevices
                .convertAndSend(redisConfigProperties.getAedEventSingleChannelPrefix()
                                + tuple2.getT1().getId() + redisConfigProperties.getAedDevicePostfix()
                        , AedDeviceInfoPreviewDto.build(tuple2.getT2()))
                .then(Mono.just(tuple2.getT1()));
    }

    public Flux<AedDeviceInfoPreviewDto> fetchPublishedAedDeviceFromSingleEvent(String channel) {
        return this.reactiveRedisTemplateAedDevices
                .listenToChannel(redisConfigProperties.getAedEventSingleChannelPrefix() +
                        channel + redisConfigProperties.getAedDevicePostfix())
                .flatMap(reactiveSubMsg -> Mono.just(reactiveSubMsg.getMessage()));
    }
}
