package com.droukos.authservice.router;

import com.droukos.authservice.controller.AuthHandlerHttp;
import com.droukos.authservice.environment.services.LvL0_Services;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.droukos.authservice.environment.services.lvl1_services.EnAuthServices.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthRouter {

    @Bean
    public RouterFunction<ServerResponse> authRoute(AuthHandlerHttp authHandler) {
        return route()
                .path(LvL0_Services.AUTH.getUrl(), builder -> builder
                        .POST(LOGIN.getUrl()            , accept(APPLICATION_JSON), authHandler::login)
                        .PUT(ACCESS_TOKEN.getUrl()      , accept(APPLICATION_JSON), authHandler::accessToken)
                        .PUT(USER_DATA.getUrl()         , accept(APPLICATION_JSON), authHandler::userData)
                        .PUT(PASSWORD_CHANGE.getUrl()   , accept(APPLICATION_JSON), authHandler::passwordChange)
                        .PUT(REVOKE_TOKENS.getUrl()     , accept(APPLICATION_JSON), authHandler::revokeTokens)
                        .PUT(PUT_ROLE_ADD.getUrl()      , accept(APPLICATION_JSON), authHandler::userRoleAdd)
                        .PUT(PUT_ROLE_DEL.getUrl()      , accept(APPLICATION_JSON), authHandler::userRoleDel)
                        .PUT(LOGOUT.getUrl()            , accept(APPLICATION_JSON), authHandler::logout)
                ).build();
    }
}
