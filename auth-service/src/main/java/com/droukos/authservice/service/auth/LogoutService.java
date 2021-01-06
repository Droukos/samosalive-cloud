package com.droukos.authservice.service.auth;

import com.droukos.authservice.environment.security.TokenService;
import com.droukos.authservice.model.factories.user.res.UserFactoryLogout;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.repo.UserRepository;
import com.droukos.authservice.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;


import static com.droukos.authservice.environment.constants.Platforms.*;
import static com.droukos.authservice.util.factories.HttpBodyBuilderFactory.okJson;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Service
@AllArgsConstructor
public class LogoutService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public Mono<Tuple2<UserRes, SecurityContext>> updateUser(Tuple2<UserRes, SecurityContext> tuple2) {
        return Mono.zip(
                Mono.just(
                        switch (SecurityUtil.getRequesterDevice(tuple2.getT2())) {
                            case IOS -> UserFactoryLogout.iosLogout(tuple2.getT1());
                            case WEB -> UserFactoryLogout.webLogout(tuple2.getT1());
                            default -> UserFactoryLogout.androidLogout(tuple2.getT1());
                        }),
                Mono.just(tuple2.getT2()));
    }

    public Mono<UserRes> removeAccessTokenIdFromRedis(Tuple2<UserRes, SecurityContext> tuple2) {

        return tokenService.redisRemoveUserToken(
                tuple2.getT1(),
                SecurityUtil.getRequesterDevice(tuple2.getT2())
        ).then(Mono.just(tuple2.getT1()));
    }


    public Mono<UserRes> saveUserToMongoDb(UserRes user) {
        return userRepository.save(user);
    }

    public Mono<ServerResponse> logoutUser() {
        return okJson()
                .cookie(tokenService.removeRefreshHttpCookie())
                .body(fromValue("user.logout"));
    }
}
