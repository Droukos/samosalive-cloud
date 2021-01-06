package com.droukos.authservice.service.auth;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.environment.dto.client.auth.login.LoginRequest;
import com.droukos.authservice.environment.dto.server.auth.login.LoginResponse;
import com.droukos.authservice.environment.security.TokenService;
import com.droukos.authservice.model.factories.user.res.UserFactoryLogin;
import com.droukos.authservice.model.factories.user.security.status.UserStatusFactory;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.security.AccountStatus;
import com.droukos.authservice.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import java.time.LocalDateTime;

import static com.droukos.authservice.environment.constants.Platforms.ANDROID;
import static com.droukos.authservice.environment.constants.Platforms.IOS;
import static com.droukos.authservice.environment.enums.AccountStatus.PERM_BANNED;
import static com.droukos.authservice.environment.enums.AccountStatus.TEMP_BANNED;
import static com.droukos.authservice.util.factories.HttpBodyBuilderFactory.okJson;
import static com.droukos.authservice.util.factories.HttpExceptionFactory.badRequest;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Service
@AllArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Mono<UserRes> validatePassword(Tuple2<LoginRequest, UserRes> objects) {

        return !bCryptPasswordEncoder.matches(
                objects.getT1().getPass(),
                objects.getT2().getPass())
                ? Mono.error(badRequest())
                : Mono.just(objects.getT2());
    }

    public Mono<NewAccTokenData> createNewAccessToken(UserRes user, String os) {
        return tokenService.genNewAccessToken(user, os);
    }

    public Mono<NewRefTokenData> createNewRefreshToken(UserRes user, String os) {
        return tokenService.genNewRefreshToken(user, os);
    }

    public Mono<Tuple2<LoginRequest, UserRes>> validateUserStatus(Tuple2<LoginRequest, UserRes> tuple2) {
        AccountStatus accountStatus = tuple2.getT2().getAccountStatusModel();
        if (tuple2.getT2().getAccountStatusModel() == null) {
            return Mono.zip(
                    Mono.just(tuple2.getT1()),
                    UserStatusFactory.createAccountStatusMono(tuple2.getT2())
            );
        }
        return accountStatus.getStat() == PERM_BANNED.getCode()
                || (accountStatus.getStat() == TEMP_BANNED.getCode() && accountStatus.getUntil().isAfter(LocalDateTime.now()))
                ? Mono.error(badRequest("Account is banned"))
                : Mono.just(tuple2);
    }

    public final Mono<Tuple3<UserRes, NewAccTokenData, NewRefTokenData>>
    saveAccessTokenIdToRedis(Tuple3<UserRes, NewAccTokenData, NewRefTokenData> tuple3) {

        return tokenService.redisSetUserToken(tuple3.getT1(), tuple3.getT2())
                .then(Mono.just(tuple3));
    }

    public final Mono<Tuple3<UserRes, NewAccTokenData, NewRefTokenData>> saveThisLastLogin
            (Tuple3<UserRes, NewAccTokenData, NewRefTokenData> tuple3) {

        return Mono.zip(
                userRepository.save(
                        switch (tuple3.getT2().getUserDevice()) {
                            case ANDROID -> UserFactoryLogin.androidLoginUpdate(tuple3);
                            case IOS -> UserFactoryLogin.iosLoginUpdate(tuple3);
                            default -> UserFactoryLogin.webLoginUpdate(tuple3);
                        }),
                Mono.just(tuple3.getT2()), Mono.just(tuple3.getT3()));
    }

    public Mono<ServerResponse> loggedInUser(Tuple3<UserRes, NewAccTokenData, NewRefTokenData> tuple3) {
        return okJson()
                .cookie(tokenService.refreshHttpCookie(tuple3.getT3()))
                .body(fromValue(LoginResponse.build(tuple3.getT1(), tuple3.getT2().getToken())));
    }
}
