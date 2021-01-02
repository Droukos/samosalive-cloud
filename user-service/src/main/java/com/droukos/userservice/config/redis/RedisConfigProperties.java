package com.droukos.userservice.config.redis;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class RedisConfigProperties {
    @Value("${aed.redis.events.general-live-channel}")
    private String aedEventLiveChannel;
    @Value("${aed.redis.events.single-live-channel-prefix}")
    private String aedEventSingleChannelPrefix;
    @Value("${aed.redis.problems.general-live-channel}")
    private String aedProblemLiveChannel;
    @Value("${aed.redis.problems.single-live-channel-prefix}")
    private String aedProblemSingleChannelPrefix;

    @Value("${aed.redis.rescuer.single-live-channel-postfix}")
    private String rescuerPostfix;
    @Value("${aed.redis.user.single-live-channel-postfix}")
    private String userPostfix;
}
