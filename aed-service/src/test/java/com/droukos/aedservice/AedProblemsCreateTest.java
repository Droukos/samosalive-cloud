package com.droukos.aedservice;

import com.droukos.aedservice.config.jwt.AccessTokenConfig;
import com.droukos.aedservice.config.jwt.ClaimsConfig;
import com.droukos.aedservice.environment.dto.client.aed_problems.AedProblemsDtoCreate;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Mono;

import java.net.URI;

class AedProblemsCreateTest {
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
    void createAedProblems(){
        AedProblemsDtoCreate aedProblemsDtoCreate = new AedProblemsDtoCreate("123","tom","ttle","addre","fwe","pending");
        Mono<AedProblems> result =
                requester
                        .route("aed.event.createEvent")
                        .metadata(TokenUtilTest.accessToken, TokenUtilTest.mimeType)
                        .data(aedProblemsDtoCreate)
                        .retrieveMono(AedProblems.class);

        //System.out.println(result.blockFirst());

        System.out.println(result.block());
    }
}
