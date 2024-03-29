package com.droukos.aedservice;

import com.droukos.aedservice.config.jwt.AccessTokenConfig;
import com.droukos.aedservice.config.jwt.ClaimsConfig;
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
class UserServiceApplicationTests {

  private static RSocketRequester requester;

  @BeforeAll
  public static void setupOnce(
          @Autowired RSocketRequester.Builder builder,
          @Autowired RSocketStrategies rsocketStrategies,
          @Autowired ClaimsConfig claimsConfig,
          @Autowired AccessTokenConfig accessTokenConfig,
          @Autowired  ReactiveStringRedisTemplate redisTemplate,
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
  void test1() {

    //Mono<String> result =
    //    requester.route("hello").metadata(creds, mimeType).retrieveMono(String.class);
//
    //System.out.println(result.block());

    // StepVerifier
    //        .create(result)
    //        .consumeNextWith(message -> {
    //            //assertThat(message.getOrigin()).isEqualTo("here");
    //            //assertThat(message.getIndex()).isEqualTo(0);})
    //        .verifyComplete();
  }

  //@Test
  //void test2() {
  //  Mono<RequestedUserInfo> result =
  //      requester
  //          .route("user.filterById." + TokenUtilTest.userId)
  //          .metadata(TokenUtilTest.accessToken, TokenUtilTest.mimeType)
  //          .retrieveMono(RequestedUserInfo.class);
//
  //  System.out.println(result.block());
  //}

  @Test
  void test3() {
    Flux<String> result =
            requester
                    .route("user.getUsernameLike." + TokenUtilTest.username)
                    .metadata(TokenUtilTest.accessToken, TokenUtilTest.mimeType)
                    .retrieveFlux(String.class);

    //System.out.println(result.blockFirst());

    result.doOnNext(System.out::println).blockLast();
  }

  //@Test
  //void test4() {
  //  Flux<RequestedPreviewUser> result =
  //          requester
  //                  .route("user.getPreview." + TokenUtilTest.username)
  //                  .metadata(TokenUtilTest.accessToken, TokenUtilTest.mimeType)
  //                  .retrieveFlux(RequestedPreviewUser.class);
//
  //  //System.out.println(result.blockFirst());
//
  //  result.doOnNext(System.out::println).blockLast();
  //}
}
