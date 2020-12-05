package com.droukos.aedservice.config.redis;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class RedisConfigProperties {
    @Value("${aedevent.redis.live-channel}")
    private String aedEventLiveChannel;
}
