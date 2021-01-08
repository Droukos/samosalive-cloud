package com.droukos.newsservice;

import com.droukos.newsservice.config.jwt.AccessTokenConfig;
import com.droukos.newsservice.config.jwt.ClaimsConfig;
import com.droukos.newsservice.environment.dto.client.NewsDtoCreate;
import com.droukos.newsservice.model.news.News;
import com.droukos.newsservice.RedisUtil;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class NewsCreateTest {
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
    void createNews(){
        List tag = new ArrayList<Integer>();
        tag.add(0);
        NewsDtoCreate newsDtoCreate  = new NewsDtoCreate("tom","new","test", tag);
        Mono<Boolean> result =
                requester
                        .route("news.post")
                        .metadata(TokenUtilTest.accessToken, TokenUtilTest.mimeType)
                        .data(newsDtoCreate)
                        .retrieveMono(Boolean.class);
        System.out.println(result.block());
    }
}
