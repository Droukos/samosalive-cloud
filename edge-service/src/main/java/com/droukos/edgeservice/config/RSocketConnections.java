package com.droukos.edgeservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;

@Configuration
public class RSocketConnections {

    @Bean
    RSocketRequester rSocketRequester(RSocketRequester.Builder builder) {
        return builder
                .connectTcp("localhost", 8888)
                .block();
    }
}
