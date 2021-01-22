package com.droukos.aedservice.problems;

import com.droukos.aedservice.RedisUtil;
import com.droukos.aedservice.TokenUtilTest;
import com.droukos.aedservice.config.jwt.AccessTokenConfig;
import com.droukos.aedservice.config.jwt.ClaimsConfig;
import com.droukos.aedservice.environment.constants.AedProblemsCodes;
import com.droukos.aedservice.environment.dto.client.aed_problems.AedProblemsDtoTechnicalSub;
import com.droukos.aedservice.model.aed_problems.AedProblems;
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
public class AedProblemsSubTechnicalTest {
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
    void subTechnical(){
        AedProblemsDtoTechnicalSub aedProblemsDtoTechnicalSub = new AedProblemsDtoTechnicalSub("60096bf9a3721902aa29a89d", "tom");
        Mono<AedProblems> result =
                requester
                        .route("aed.problems.subTechnical")
                        .metadata(TokenUtilTest.accessToken, TokenUtilTest.mimeType)
                        .data(aedProblemsDtoTechnicalSub)
                        .retrieveMono(AedProblems.class);

        System.out.println(result.block());
    }
}
