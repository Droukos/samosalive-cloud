package com.droukos.aedservice;

import com.droukos.aedservice.config.jwt.AccessTokenConfig;
import com.droukos.aedservice.config.jwt.ClaimsConfig;
import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceRegisterDto;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoCreate;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.crypto.MacProvider;
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

import javax.crypto.SecretKey;
import java.net.URI;

@SpringBootTest
public class AedDeviceCreateTest {
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
    void createAedDevice(){
        System.out.println(TokenUtilTest.accessToken);
        //System.out.println("hey");
        //SecretKey key = MacProvider.generateKey(SignatureAlgorithm.HS256);
        //System.out.println(key);
        //String base64Encoded = TextCodec.BASE64.encode(key.getEncoded());
        //System.out.println(base64Encoded);
        //AedDeviceRegisterDto aedDeviceRegisterDto = new AedDeviceRegisterDto("123","tom","1",23.44,27.666,"pending");
        //Mono<String> result =
        //        requester
        //                .route("aed.register.device")
        //                .metadata(TokenUtilTest.accessToken, TokenUtilTest.mimeType)
        //                .data(aedDeviceRegisterDto)
        //                .retrieveMono(String.class);
//
        //System.out.println(result.block());
    }
}
