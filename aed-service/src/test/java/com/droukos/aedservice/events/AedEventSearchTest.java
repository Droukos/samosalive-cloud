package com.droukos.aedservice.events;

import com.droukos.aedservice.RedisUtil;
import com.droukos.aedservice.TokenUtilTest;
import com.droukos.aedservice.config.jwt.AccessTokenConfig;
import com.droukos.aedservice.config.jwt.ClaimsConfig;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoSearch;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.RequestedAedEvent;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.RequestedPreviewAedEvent;
import com.droukos.aedservice.model.user.UserRes;
import com.droukos.aedservice.repo.UserRepository;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class AedEventSearchTest {
    private static RSocketRequester requester;
    @Autowired private UserRepository userRepository;

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
    void findAedEvent(){
        AedEventDtoSearch aedEventDtoSearch= new AedEventDtoSearch(0,1);
        Flux<RequestedPreviewAedEvent> result =
                requester
                        .route("aed.event.get")
                        .metadata(TokenUtilTest.accessToken, TokenUtilTest.mimeType)
                        .data(aedEventDtoSearch)
                        .retrieveFlux(RequestedPreviewAedEvent.class);

        result.doOnNext(System.out::println).blockLast();
    }

    @Test
    void test1() {
        List<String> usernames = Arrays.asList("kostas", "kostass");
       userRepository.findAllByUserIn(usernames)
                .collect(Collectors.toList())
                .flatMap(list -> {
                    list.forEach(System.out::println);
                    return Mono.just(list);
                })
                .block();
    }
}
