package com.droukos.authservice.service.auth;

import com.droukos.authservice.environment.dto.server.auth.login.LoginResponse;
import com.droukos.authservice.environment.security.TokenService;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.repo.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

import static com.droukos.authservice.environment.constants.Platforms.ANDROID;
import static com.droukos.authservice.environment.constants.Platforms.IOS;
import static com.droukos.authservice.environment.security.HttpBodyBuilderFactory.okJson;
import static com.droukos.authservice.environment.security.HttpExceptionFactory.badRequest;
import static java.time.LocalDateTime.now;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Service
@RequiredArgsConstructor
public class LoginService {

  @NonNull private final UserRepository userRepository;
  @NonNull private final TokenService tokenService;

  private final Predicate<UserRes> passwordNotMatch =
          user -> !new BCryptPasswordEncoder().matches(user.getLoginRequest().getPass(), user.getPass());

  public Mono<UserRes> validatePassword(UserRes user) {

    return passwordNotMatch.test(user)
        ? Mono.error(badRequest())
        : Mono.just(user);
  }

  public void createNewAccessToken(UserRes user) {
    user.setAccessToken(tokenService.genNewAccessToken(user));
  }

  public final void createNewRefreshToken(UserRes user) {
    user.setRefreshToken(tokenService.genNewRefreshToken(user));
  }

  public final Mono<UserRes> saveAccessTokenIdToRedis(UserRes user) {
    return tokenService.redisSetUserToken(user, user.getAccessToken()).then(Mono.just(user));
  }

  public final void setUserIsNowOnline(UserRes user) {
    user.getAppState().setOn(true);
  }

  public final void saveThisLastLogin(UserRes user) {
    switch (user.getUserDevice()) {
      case ANDROID -> user.setAndroidLastLogin(now());
      case IOS -> user.setIosLastLogin(now());
      default -> user.setWebLastLogin(now());
    }
  }

  public final void setUserNewTokens(UserRes user) {
    tokenService.updateUserWithNewTokens(user);
  }

  public Mono<ServerResponse> loggedInUser(UserRes user) {

       return userRepository.save(user)
            .flatMap(savedUser ->
                               okJson()
                                   .cookie(tokenService.refreshHttpCookie(user.getRefreshToken()))
                                   .body(fromValue(LoginResponse.build(savedUser, user.getAccessToken()))));
  }
}
