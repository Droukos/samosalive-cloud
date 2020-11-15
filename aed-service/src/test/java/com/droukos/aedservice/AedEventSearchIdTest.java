package com.droukos.aedservice;

import com.droukos.aedservice.config.jwt.AccessTokenConfig;
import com.droukos.aedservice.config.jwt.ClaimsConfig;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoIdSearch;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.RequestedPreviewAedEvent;
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
public class AedEventSearchIdTest {
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
    void findEventId(){
        String id = "5fa82d99f954c2435a9c1ca9";
        AedEventDtoIdSearch aedEventDtoIdSearch = new AedEventDtoIdSearch(id);
        Mono<RequestedPreviewAedEvent> result =
                requester
                        .route("aed.event.getId")
                        .metadata(TokenUtilTest.accessToken, TokenUtilTest.mimeType)
                        .data(aedEventDtoIdSearch)
                        .retrieveMono(RequestedPreviewAedEvent.class);

        result.doOnNext(System.out::println).block();
    }
}

