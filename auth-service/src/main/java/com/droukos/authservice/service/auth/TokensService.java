package com.droukos.authservice.service.auth;

import com.droukos.authservice.environment.dto.server.auth.login.LoginResponse;
import com.droukos.authservice.environment.dto.server.auth.token.NewAccessTokenResponse;
import com.droukos.authservice.environment.security.TokenService;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.security.jwt.platforms.AndroidJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.IosJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.WebJWT;
import com.droukos.authservice.model.user.system.security.jwt.tokens.AccessToken;
import com.droukos.authservice.repo.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@RequiredArgsConstructor
public class TokensService {

  @NonNull private final UserRepository userRepository;
  @NonNull private final TokenService tokenService;

  public void genNewAccTokenToUser(UserRes user) {
    user.setRequesterAccessTokenData(tokenService.genNewAccessToken(user));
  }

  public void genNewRefTokenToUser(UserRes user) {
    user.setRequesterRefreshTokenData(tokenService.genNewRefreshToken(user));
  }

  public void deleteAllOldJwtTokens(UserRes user) {
    user.setAndroidJwtModel(new AndroidJWT());
    user.setIosJwtModel(new IosJWT());
    user.setWebJwtModel(new WebJWT());
  }

  public void setUserNewTokens(UserRes user) {
    tokenService.updateUserWithNewTokens(user);
  }

  public Mono<UserRes> setNewAccessTokenIdToRedis(UserRes user) {
    return tokenService.redisSetUserToken(user)
            .then(Mono.just(user));
  }

  public Mono<UserRes> saveUserTokenChangesOnDb(UserRes user) {
    return userRepository.save(user);
  }

  public Mono<ServerResponse> setRefTokenOnHttpCookieAndAccessTokenOnBody(UserRes user) {
    return ok().cookie(tokenService.refreshHttpCookie(user))
        .body(fromValue(new NewAccessTokenResponse(user.getRequesterAccessTokenData().getToken())));
  }

  public void checkCaseRefreshTokenIsExpiring(UserRes userRes) {
    userRes.getRequesterRefreshTokenData()
            .setRefreshIsExpiring(tokenService.isRefreshAboutToExp(userRes));
  }

  public Mono<ServerResponse> newAccessToken(UserRes user) {

    return user.getRequesterRefreshTokenData().isRefreshIsExpiring()
                    ? ok().cookie(tokenService.refreshHttpCookie(user))
                        .body(fromValue(new NewAccessTokenResponse(user.getRequesterAccessTokenData().getToken())))
                    : ok().body(fromValue(new NewAccessTokenResponse(user.getRequesterAccessTokenData().getToken())));
  }

  public Mono<UserRes> validateUserRefreshToken(UserRes user) {
    return tokenService.validateRefreshToken(user);
  }

  public void setAccTokenOrTokensToUser(UserRes user) {
    tokenService.updateAccTokenOrTokens(user);
  }

  public void setAccessTokenModel(UserRes userRes) {
    tokenService.setNewAccessTokenModel(
        userRes, new AccessToken(userRes.getRequesterAccessTokenData().getToken()));
  }

  public Mono<ServerResponse> buildUserData(UserRes user) {
    return ok().body(fromValue(LoginResponse.build(user, user.getRequesterAccessTokenData().getToken())));
  }
}
