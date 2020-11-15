package com.droukos.newsservice;

import com.droukos.newsservice.config.jwt.AccessTokenConfig;
import com.droukos.newsservice.config.jwt.ClaimsConfig;
import com.droukos.newsservice.environment.dto.server.news.RequestedNews;
import com.droukos.newsservice.environment.dto.server.news.RequestedPreviewNews;
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
public class NewsFindTest {
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
    void findNews(){
        String id = "5f9198c8f7c8492f2ac442a3";
        Mono<RequestedNews> result =
                requester
                .route("news.get")
                .metadata(TokenUtilTest.accessToken, TokenUtilTest.mimeType)
                .data(id)
                .retrieveMono(RequestedNews.class);

        result.doOnNext(System.out::println).block();
    }
}
