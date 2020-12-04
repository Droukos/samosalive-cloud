package com.droukos.aedservice;

import com.droukos.aedservice.config.jwt.AccessTokenConfig;
import com.droukos.aedservice.config.jwt.ClaimsConfig;
import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceAreaSearchDto;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.repo.AedDeviceRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@SpringBootTest
public class AedDeviceSearchTest {
    private static RSocketRequester requester;

    @Autowired
    private AedDeviceRepository aedDeviceRepository;

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
    void searchAedDeviceInArea(){
        double x = 10.594796841892734;
        double y = 44.608664;
        //AedDevice aedDevice = new AedDevice(null, "sdsddse", "sdsdds", "sdsdsd", LocalDateTime.now(), "me", 0, "sds", new GeoJsonPoint(x,y), "", "", "", null, "", "", null, 0 );
//
        //aedDeviceRepository.save(aedDevice).subscribe();
        aedDeviceRepository.getAedDevicesByHomePNear(new GeoJsonPoint(x,y), new Distance(2, Metrics.KILOMETERS))
                .defaultIfEmpty(new AedDevice())
                .flatMap(tempAedDevice -> {
                    System.out.println(tempAedDevice);
                    return Mono.just(tempAedDevice);
                }).subscribe();
    }

    @Test
    void searchAedDeviceInAreaController() {
        double x = 10.594796841892734;
        double y = 44.608664;

        Flux<AedDevice> result = requester
                        .route("aed.device.fetch.inArea")
                .metadata(TokenUtilTest.accessToken, TokenUtilTest.mimeType)
                .data(new AedDeviceAreaSearchDto(x, y, 10))
                .retrieveFlux(AedDevice.class);

        result.doOnNext(System.out::println).blockLast();
    }
}
