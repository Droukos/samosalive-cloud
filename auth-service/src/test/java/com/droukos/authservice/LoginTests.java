package com.droukos.authservice;

import com.droukos.authservice.environment.dto.client.auth.login.LoginRequest;
import com.droukos.authservice.model.user.personal.AddressModel;
import com.droukos.authservice.repo.UserRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static com.droukos.authservice.environment.services.lvl1_services.EnAuthServices.LOGIN;

@SpringBootTest
@AutoConfigureWebTestClient
class LoginTests {

    //@MockBean
    //UserRepository userRepository;

    @Autowired
    private WebTestClient webClient;

    @Test
    void loginOnAllLayers() {
        LoginRequest loginRequest = new LoginRequest("kostas","xaxaxa96");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonStr = gson.toJson(loginRequest);
        //Mockito.when(userRepository.findFirstByUser("kostass").thenReturn(Mono.just()));

        webClient.post()
                .uri(LOGIN.getFullUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonStr))
                .exchange()
                .expectStatus().isOk();

        //userRepository.findFirstByUser("kkkosstas")
        //        .flatMap()
    }
}
