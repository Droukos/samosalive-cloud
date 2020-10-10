package com.droukos.authservice;

import com.droukos.authservice.environment.dto.client.auth.SignupInfo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SignupTest {

  private static RSocketRequester requester;
  final MimeType mimeType = new MediaType("message", "x.rsocket.authentication.bearer.v0");
  String creds =
      "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrb3N0YXMiLCJ1aWQiOiI1ZjdiZDJkODQ3NzA2NTMzNDExZTVlNTUiLCJzY29wZXMiOlsiR0VORVJBTF9BRE1JTiJdLCJpZGVudGlmaWVyIjoiMmFmYzQwM2MtZjQ3Ny00MzI2LTkzNmQtYWJkMjFmM2YzYTlkIiwiZGV2aWNlLW9zIjoid2ViIiwiaWF0IjoxNjAyMjY0MTMwLCJleHAiOjE2MDIyNjUzMzB9.OFajQ4BcaSIZF1c739FGsPGwYMueEAieTKX8M8P9CPA";
  String id = "5f7bd2d847706533411e5e55";
  SignupInfo goodSignupInfo =
      new SignupInfo("kostass", "xaxaxa96", "xaxaxa96", "Kos", "Dr", "kostass13@gmail.com");
  SignupInfo badUsernameSInfo =
      new SignupInfo("kostas!s", "xaxaxa96", "xaxaxa96", "Kos", "Dr", "kostass13@gmail.com");
  SignupInfo badPassMatchSignupInfo =
      new SignupInfo("kostass", "xaxaxa96", "xaxax96", "Kos", "Dr", "kostass13@gmail.com");

  @BeforeAll
  public static void setupOnce(
      @Autowired RSocketRequester.Builder builder,
      @Autowired RSocketStrategies rsocketStrategies,
      @Value("${spring.rsocket.server.port}") Integer port) {

    requester = builder
            .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
            .rsocketStrategies(rsocketStrategies)
            .connectWebSocket(URI.create("ws://localhost:" + port))
            .block();
  }

  @AfterAll
  public static void tearDownOnce() {
    requester.rsocket().dispose();
  }
  // SignupInfo empty

  @Test
  void testSignup() {

    Mono<Boolean> result =
        requester.route("auth.signup").data(badPassMatchSignupInfo).retrieveMono(Boolean.class);

    StepVerifier.create(result)
        .expectErrorSatisfies(
            err -> assertEquals(HttpStatus.BAD_REQUEST, ((ResponseStatusException) err).getStatus()))
        .verify();
    // StepVerifier
    //        .create(result)
    //        .consumeNextWith(message -> {
    //            //assertThat(message.getOrigin()).isEqualTo("here");
    //            //assertThat(message.getIndex()).isEqualTo(0);})
    //        .verifyComplete();
  }
}
