package com.droukos.authservice;

import com.droukos.authservice.environment.dto.client.auth.SignupInfo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Mono;

import java.net.URI;

@SpringBootTest
class SignupTest {

    private static RSocketRequester requester;

    @BeforeAll
    public static void setupOnce(
            @Autowired RSocketRequester.Builder builder,
            @Value("${spring.rsocket.server.port}") Integer port) {

        requester = builder.connectWebSocket(URI.create("ws://localhost:" + port)).block();
    }

    @AfterAll
    public static void tearDownOnce() {
        requester.rsocket().dispose();
    }

    @Test
    void testSignup() {

    Mono<Boolean> result =
        requester
            .route("auth.signup")
            .data(
                new SignupInfo("kostas", "xaxaxa96", "xaxaxa96", "Kos", "Dr", "kostas13@gmail.com"))
            .retrieveMono(Boolean.class);

    System.out.println(result.block());
    // StepVerifier
    //        .create(result)
    //        .consumeNextWith(message -> {
    //            //assertThat(message.getOrigin()).isEqualTo("here");
    //            //assertThat(message.getIndex()).isEqualTo(0);})
    //        .verifyComplete();
  }
}
