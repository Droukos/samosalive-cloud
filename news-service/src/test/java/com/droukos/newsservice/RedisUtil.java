package com.droukos.newsservice;

import com.droukos.newsservice.config.jwt.AccessTokenConfig;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

import static java.time.Duration.ofMinutes;

public class RedisUtil {

    public static void setRedisToken(ReactiveStringRedisTemplate redisTemplate, AccessTokenConfig accessTokenConfig) {
        redisTemplate.opsForValue()
                .set(TokenUtilTest.userId+"-"+TokenUtilTest.platform,
                        TokenUtilTest.tokenId,
                        ofMinutes(accessTokenConfig.getValidMinutesAll()))
                .block();
    }
}
