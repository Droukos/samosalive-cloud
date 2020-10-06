package com.droukos.authservice.environment.security;

import com.droukos.authservice.config.jwt.AccessTokenConfig;
import com.droukos.authservice.config.jwt.ClaimsConfig;
import com.droukos.authservice.config.jwt.RefreshTokenConfig;
import com.droukos.authservice.environment.dto.RequesterAccessTokenData;
import com.droukos.authservice.environment.dto.RequesterRefreshTokenData;
import com.droukos.authservice.environment.interfaces.JwtToken;
import com.droukos.authservice.environment.security.tokens.AccessJwtService;
import com.droukos.authservice.environment.security.tokens.RefreshJwtService;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.security.jwt.platforms.AndroidJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.IosJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.WebJWT;
import com.droukos.authservice.model.user.system.security.jwt.tokens.AccessToken;
import com.droukos.authservice.model.user.system.security.jwt.tokens.RefreshToken;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.function.*;

import static com.droukos.authservice.environment.constants.Platforms.ANDROID;
import static com.droukos.authservice.environment.constants.Platforms.IOS;
import static com.droukos.authservice.util.factories.HttpExceptionFactory.unauthorized;
import static com.droukos.authservice.util.RedisUtil.redisTokenK;
import static java.time.Duration.ofMinutes;

@Service
@RequiredArgsConstructor
public class TokenService {

  @NonNull private final ReactiveStringRedisTemplate redisTemplate;
  @NonNull private final RefreshJwtService refreshJwtService;
  @NonNull private final AccessJwtService accessJwtService;
  @NonNull private final ClaimsConfig claimsConfig;
  @NonNull private final AccessTokenConfig accessTokenConfig;
  @NonNull private final RefreshTokenConfig refreshTokenConfig;

  public Mono<UserRes> validateRefreshToken(UserRes user) {

    Predicate<JwtToken> validRefreshTokenId = jwtToken -> jwtToken.getRefreshTokenId()
        .equals(user.getRequesterRefreshTokenData().getTokenId());

    Predicate<JwtToken> jwtTokenIsNull = Objects::isNull;
    Predicate<JwtToken> refreshTokenModelIsNull = jwtToken -> jwtToken.getRefreshTokenModel() == null;
    Predicate<JwtToken> refreshTokenIdIsNull = jwtToken -> jwtToken.getRefreshTokenId() == null;
    Predicate<JwtToken> refreshTokenIsNull = jwtToken -> jwtTokenIsNull
        .or(refreshTokenModelIsNull.or(refreshTokenIdIsNull)).test(jwtToken);

    return validateToken(user.getRequesterRefreshTokenData().getUserDevice(), user, jwtToken ->
            (!refreshTokenIsNull.and(validRefreshTokenId).test(jwtToken))
                    ? Mono.just(user)
                    : Mono.error(unauthorized()));
  }

  public RequesterRefreshTokenData genNewRefreshToken(UserRes user) {
    var map = new HashMap<String, String>();
    map.put(claimsConfig.getTokenId(), UUID.randomUUID().toString());
    map.put(claimsConfig.getPlatform(), user.getRequesterRefreshTokenData().getUserDevice());
    return refreshJwtService.genRefreshToken(user, map);
  }

  public void deleteAccessTokensForUser(UserRes user) {
    Predicate<ServerRequest> isTheSameUser = request -> request.pathVariable("id")
        .equals(user.getRequesterAccessTokenData().getUserId());

    if (isTheSameUser.test(user.getServerRequest())) {
      String accessTokenId = user.getRequesterAccessTokenData().getTokenId();
      Predicate<JwtToken> jwtTokenNotNull = Objects::nonNull;
      Predicate<JwtToken> accessTokenModelNotNull = jwtToken -> jwtToken.getAccessTokenModel() != null;
      Predicate<JwtToken> accessTokenIdMatches = jwtToken -> jwtToken.getAccessTokenId()
              .equals(accessTokenId);
      Predicate<JwtToken> accessTokenIdExists = jwtToken -> jwtTokenNotNull.and(accessTokenModelNotNull)
              .and(accessTokenIdMatches).test(jwtToken);
      removeNullJwtModels(user, accessTokenIdExists);
      generateNewAccessTokenToUser(user);
    }

    deleteAllAccessTokenModels(user);
    user.setRequesterAccessTokenData(null);
  }

  private void removeNullJwtModels(UserRes targetUser, Predicate<JwtToken> accessTokenExists) {
    if (accessTokenExists.test(targetUser.getAndroidJwtModel())) {
      targetUser.setAndroidAccessToken(null);
    }
    if (accessTokenExists.test(targetUser.getIosJwtModel())) {
      targetUser.setIosAccessToken(null);
    }
    if (accessTokenExists.test(targetUser.getWebJwtModel())) {
      targetUser.setWebAccessToken(null);
    }
  }

  private void deleteAllAccessTokenModels(UserRes targetUser) {
    //ToDo delete from Redis access tokens
    if (targetUser.getAndroidJwtModel() != null) {
      targetUser.setAndroidAccessToken(null);
    }
    if (targetUser.getIosJwtModel() != null) {
      targetUser.setIosAccessToken(null);
    }
    if (targetUser.getWebJwtModel() != null) {
      targetUser.setWebAccessToken(null);
    }
  }


  public void setNewAccessTokenModel(UserRes userRes, AccessToken accessTokenModel) {
    switch (userRes.getRequesterAccessTokenData().getUserDevice()) {
      case ANDROID -> userRes.setAndroidAccessToken(accessTokenModel);
      case IOS -> userRes.setIosAccessToken(accessTokenModel);
      default -> userRes.setWebAccessToken(accessTokenModel);
    }
  }

  public void generateNewAccessTokenToUser(UserRes user) {
    user.setRequesterAccessTokenData(genNewAccessToken(user));
  }

  public RequesterAccessTokenData genNewAccessToken(UserRes user) {
    HashMap<String, String> map = new HashMap<>();
    map.put(claimsConfig.getTokenId(), UUID.randomUUID().toString());
    map.put(claimsConfig.getPlatform(), user.getRequesterAccessTokenData().getUserDevice());
    return accessJwtService.generateAccessToken(user, map);
  }

  public Mono<UserRes> validateToken(String userDevice, UserRes user, Function<JwtToken, Mono<UserRes>> validateToken) {
    return switch (userDevice) {
      case ANDROID -> validateToken.apply(user.getAndroidJwtModel());
      case IOS -> validateToken.apply(user.getIosJwtModel());
      default -> validateToken.apply(user.getWebJwtModel());
    };
  }

  public Mono<Boolean> redisSetUserToken(UserRes userRes) {
    return redisTemplate
            .opsForValue()
            .set(redisTokenK(userRes),
                    userRes.getRequesterAccessTokenData().getTokenId(),
                    ofMinutes(accessTokenConfig.getValidMinutesAll()));
  }

  public Mono<Boolean> redisRemoveUserToken(UserRes userRes) {
    return redisTemplate.opsForValue().delete(redisTokenK(userRes));
  }


  public void updateUserWithNewTokens(UserRes user) {
    String userIP = user.getServerRequest()
            .remoteAddress()
            .toString()
            .replace("Optional[", "")
            .replace("]", ""); //Remove Optional[*userIPAndPort*]
    RefreshToken reTokenModel = RefreshToken.builder()
            .id(user.getRequesterRefreshTokenData().getTokenId())
            .ip(userIP)
            .exp(user.getRequesterRefreshTokenData().getExpiration())
            .build();
    AccessToken accTokenModel = AccessToken.builder()
            .id(user.getRequesterAccessTokenData().getTokenId())
            .build();

    switch (user.getRequesterAccessTokenData().getUserDevice()) {
      case ANDROID -> user.setAndroidJwtModel(new AndroidJWT(reTokenModel, accTokenModel));
      case IOS -> user.setIosJwtModel(new IosJWT(reTokenModel, accTokenModel));
      default -> user.setWebJwtModel(new WebJWT(reTokenModel, accTokenModel));
    }
  }

  public void setUserNewAccToken(UserRes user) {
    switch (user.getRequesterAccessTokenData().getUserDevice()) {
      case ANDROID -> user.setAndroidAccessToken(new AccessToken(user.getRequesterAccessTokenData().getTokenId()));
      case IOS -> user.setIosAccessToken(new AccessToken(user.getRequesterAccessTokenData().getTokenId()));
      default -> user.setWebAccessToken(new AccessToken(user.getRequesterAccessTokenData().getTokenId()));
    }
  }

  public ResponseCookie refreshHttpCookie(UserRes user) {

    return switch (user.getRequesterRefreshTokenData().getUserDevice()) {
      case ANDROID, IOS ->
              ResponseCookie.from(refreshTokenConfig.getCookieName(), user.getRequesterRefreshTokenData().getToken())
                      .httpOnly(true)
                      .maxAge(refreshJwtService.getRefTokenAndroidIosExpDate().getNano())
                      //.sameSite("strict")
                      // .path("/api/auth/token")
                      .build();
      default ->
              ResponseCookie.from(refreshTokenConfig.getCookieName(), user.getRequesterRefreshTokenData().getToken())
                      .httpOnly(true)
                      .maxAge(refreshJwtService.getRefTokenWebExpDate().getNano())
                      //.sameSite("strict")
                      //.path("/api/auth/token")
                      .build();
    };
  }

  public ResponseCookie removeRefreshHttpCookie() {
    return ResponseCookie.from(refreshTokenConfig.getCookieName(), "")
        .httpOnly(true)
        .maxAge(0)
        .sameSite("strict")
        .path("/api/auth/token")
        .build();
  }

  public String getRefreshToken(ServerRequest request) {
    Supplier<ResponseStatusException> unauthorizedRefCookie = () -> {
      throw unauthorized("No RefreshToken given");
    };

    if (request.headers().header("Cookie").isEmpty()) { unauthorizedRefCookie.get(); }
    final String[] refToken = {""};

    Arrays.stream(request.headers().header("Cookie").get(0).split(";"))
        .filter(s -> s.contains(refreshTokenConfig.getCookieName()))
        .findFirst()
        .map(s -> s.replace(refreshTokenConfig.getCookieName() + "=", ""))
        .ifPresentOrElse(s -> refToken[0] = s, unauthorizedRefCookie::get);
    refreshJwtService.getUserIdClaim(refToken[0]);
    return refToken[0];
  }


  public LocalDateTime refreshTokenExp (UserRes userRes) {
    return LocalDate.from(
                    userRes.getRequesterRefreshTokenData()
                            .getExpiration()
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .atStartOfDay();
  }

  public boolean isRefreshAboutToExp(UserRes userRes) {
    LocalDateTime day100FromNow = LocalDate.now().plusDays(refreshTokenConfig.getRenewDaysAll()).atStartOfDay();
    IntPredicate refreshIsAboutToExpire = jwtRefreshDays ->
                    Duration.between(day100FromNow, refreshTokenExp(userRes)).toDays() < jwtRefreshDays;
    return refreshIsAboutToExpire.test(refreshTokenConfig.getRenewDaysAll());
  }

  public void updateAccTokenOrTokens(UserRes userRes) {
    if (isRefreshAboutToExp(userRes)) {
      updateUserWithNewTokens(userRes);
    } else {
      setUserNewAccToken(userRes);
    }
  }

  public String refTokenFromHttpCookie(UserRes user) {
    return Objects.requireNonNull(user.getServerRequest().cookies().getFirst(refreshTokenConfig.getCookieName()))
            .getValue();
  }
}
