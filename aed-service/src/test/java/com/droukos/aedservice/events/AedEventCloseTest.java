package com.droukos.aedservice.events;

import com.droukos.aedservice.RedisUtil;
import com.droukos.aedservice.TokenUtilTest;
import com.droukos.aedservice.config.jwt.AccessTokenConfig;
import com.droukos.aedservice.config.jwt.ClaimsConfig;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoClose;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoRescuerSub;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Mono;

import java.net.URI;

@SpringBootTest
public class AedEventCloseTest {
    private static RSocketRequester requester;

    @BeforeAll
    public static void setupOnce(
            @Autowired RSocketRequester.Builder builder,
            @Autowired RSocketStrategies rsocketStrategies,
            @Autowired ClaimsConfig claimsConfig,
            @Autowired AccessTokenConfig accessTokenConfig,
            @Autowired ReactiveStringRedisTemplate redisTemplate,
            @Value("${spring.rsocket.server.port}") Integer port) {

        TokenUtilTest.accessToken = TokenUtilTest.tokenGenerator(claimsConfig, accessTokenConfig);
        RedisUtil.setRedisToken(redisTemplate, accessTokenConfig);

        requester =
                builder
                        .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                        .rsocketStrategies(rsocketStrategies)
                        .connectWebSocket(URI.create("ws://localhost:" + port))
                        .block();
    }

    @AfterAll
    public static void tearDownOnce() {
        requester.rsocket().dispose();
    }

    @Test
    void closeAedEvent(){
        AedEventDtoClose aedEventDtoClose= new AedEventDtoClose("5fb57b086730d175277fb50f", "everything was ok");
        Mono<Boolean> result =
                requester
                        .route("aed.event.close")
                        .metadata(TokenUtilTest.accessToken, TokenUtilTest.mimeType)
                        .data(aedEventDtoClose)
                        .retrieveMono(Boolean.class);

        System.out.println(result.block());
    }
}
