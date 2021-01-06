package com.droukos.aedservice;

import com.droukos.aedservice.config.jwt.AccessTokenConfig;
import com.droukos.aedservice.config.jwt.ClaimsConfig;
import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceRegisterDto;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventRescuerDeviceSearch;
import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceRescuer;
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

@SpringBootTest
public class AedDeviceInfoTest {
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
    void aedDeviceInfo(){
        AedDeviceRegisterDto aedDeviceRegisterDto = new AedDeviceRegisterDto("123","tom","1",23.44,27.666,"pending");
        Mono<String> result =
                requester
                        .route("aed.device.fetch.preview.byNickname")
                        .metadata(TokenUtilTest.accessToken, TokenUtilTest.mimeType)
                        .data(aedDeviceRegisterDto)
                        .retrieveMono(String.class);

        System.out.println(result.block());
    }

    @Test
    void checkAggregation() {

    }

    @Test
    void aedDeviceRescuerInfo(){
        AedEventRescuerDeviceSearch aedDeviceRegisterDto = new AedEventRescuerDeviceSearch("5fe232cce23f4d26d9cd370e");
        Flux<AedDeviceRescuer> result =
                requester
                        .route("aed.event.fetch.rescuer.and.device")
                        .metadata(TokenUtilTest.accessToken, TokenUtilTest.mimeType)
                        .data(aedDeviceRegisterDto)
                        .retrieveFlux(AedDeviceRescuer.class);

        System.out.println(result.blockLast());
    }
}