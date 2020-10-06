package com.droukos.authservice.service.auth;

import com.droukos.authservice.environment.security.TokenService;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.repo.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.droukos.authservice.environment.constants.Platforms.ANDROID;
import static com.droukos.authservice.environment.constants.Platforms.IOS;
import static com.droukos.authservice.util.factories.HttpBodyBuilderFactory.okJson;
import static java.time.LocalDateTime.now;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Service
@RequiredArgsConstructor
public class LogoutService {

    @NonNull private final UserRepository userRepository;
    @NonNull private final TokenService tokenService;

    private final Consumer<UserRes> userIsOffline = userToCheck -> userToCheck.getAppState().setOn(false);
    private final Predicate<UserRes> androidJwtModelIsNull = userToCheck -> userToCheck.getAndroidJwtModel() == null;
    private final Predicate<UserRes> iosJwtModelIsNull = userToCheck -> userToCheck.getIosJwtModel() == null;
    private final Predicate<UserRes> webJwtModelIsNull = userToCheck -> userToCheck.getWebJwtModel() == null;

    public void nullifyUserJwtModel(UserRes user) {
        switch (user.getRequesterAccessTokenData().getUserDevice()) {
            case ANDROID -> user.setAndroidJwtModel(null);
            case IOS -> user.setIosJwtModel(null);
            default -> user.setWebJwtModel(null);
        }
    }

    public void caseNoJwtModelsLeftSetUserOffline(UserRes user) {
        if (androidJwtModelIsNull.and(iosJwtModelIsNull).and(webJwtModelIsNull).test(user)) {
            userIsOffline.accept(user);
        }
    }

    public void saveThisLogoutTime(UserRes user) {
        switch (user.getRequesterAccessTokenData().getUserDevice()) {
            case ANDROID -> user.setAndroidLastLogout(now());
            case IOS -> user.setIosLastLogout(now());
            default -> user.setWebLastLogout(now());
        }}

    public Mono<UserRes> removeAccessTokenIdFromRedis(UserRes user) {
        return tokenService.redisRemoveUserToken(user)
                .then(Mono.just(user));
    }

    public Mono<ServerResponse> logoutUser(UserRes user) {
        return userRepository.save(user)
                .then(okJson().cookie(tokenService.removeRefreshHttpCookie()).body(fromValue("user.logout")));
    }
}
