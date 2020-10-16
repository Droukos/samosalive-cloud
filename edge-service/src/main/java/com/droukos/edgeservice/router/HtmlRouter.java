package com.droukos.edgeservice.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class HtmlRouter {

    @Bean
    public RouterFunction<ServerResponse> htmlRouting() {
        return route(GET("/"), request -> indexHtml());
    }

    @Bean
    public RouterFunction<ServerResponse> jsRouting() {
        return RouterFunctions.resources("/js/**", new ClassPathResource("js/"));
    }

    @Bean
    public RouterFunction<ServerResponse> forwardRouting() {
        return route(GET("{_:^(?!index\\.html|api).*$}"), req -> indexHtml());
    }

    private Mono<ServerResponse> indexHtml() {
        return ok().render("index", Rendering.view("index").build());
    }
}