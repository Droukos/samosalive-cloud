package com.droukos.authservice.environment.security;

import com.droukos.authservice.environment.interfaces.JwtToken;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.security.jwt.platforms.AndroidJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.IosJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.WebJWT;
import com.droukos.authservice.model.user.system.security.jwt.tokens.AccessToken;
import com.droukos.authservice.model.user.system.security.jwt.tokens.RefreshToken;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import static com.droukos.authservice.environment.security.HttpExceptionFactory.unauthorized;
import static com.droukos.authservice.util.RedisUtil.redisTokenK;
import static java.time.Duration.ofMinutes;

@Service
@RequiredArgsConstructor
public class TokenService {

  @NonNull private final ReactiveStringRedisTemplate redisTemplate;
  @NonNull private final JwtService jwtService;
  @Value("${jwt.identifier}") public String IDENTIFIER;
  @Value("${user.platform}") public String USER_DEVICE;
  @Value("${jwt.prefix.bearer}") public String TOKEN_PREFIX;
  @Value("${jwt.limiter.forweb}") public int WEB_JWT_LIMITER;
  @Value("${jwt.refresh_token_validity_fornative}") public String REFRESH_TOKEN_VALIDITY_FOR_NATIVE;
  @Value("${jwt.refresh_token_validity_forweb}") public String REFRESH_TOKEN_VALIDITY_FOR_WEB;
  @Value("${jwt.access_token_validity}") private long accessTokenMinValidity;
  @Value("${jwt.refresh.days}") private int JWT_REFRESH_DAYS;
  @Value("${jwt.refresh.key}") private String REFRESH_TOKEN_KEY;

  public Mono<UserRes> validateRefreshToken(UserRes user) {
    String refToken = this.getRefreshToken(user.getServerRequest());
    Predicate<JwtToken> validRefreshTokenId = jwtToken -> jwtToken.getRefreshTokenId()
        .equals(jwtService.getTokenId(refToken));

    Predicate<JwtToken> jwtTokenIsNull = Objects::isNull;
    Predicate<JwtToken> refreshTokenModelIsNull = jwtToken -> jwtToken.getRefreshTokenModel() == null;
    Predicate<JwtToken> refreshTokenIdIsNull = jwtToken -> jwtToken.getRefreshTokenId() == null;
    Predicate<JwtToken> refreshTokenIsNull = jwtToken -> jwtTokenIsNull
        .or(refreshTokenModelIsNull.or(refreshTokenIdIsNull)).test(jwtToken);

    return validateToken(jwtService.getDevice(refToken), user, jwtToken ->
            (!refreshTokenIsNull.and(validRefreshTokenId).test(jwtToken))
                    ? Mono.just(user)
                    : Mono.error(unauthorized()));
  }

  public Mono<UserRes> validateAccessToken(UserRes user, String token) {
    Predicate<JwtToken> jwtTokenIsNull = Objects::isNull;
    Predicate<JwtToken> accessTokenModelIsNull = jwtToken -> jwtToken.getAccessTokenModel() == null;
    Predicate<JwtToken> accessTokenIdIsNull = jwtToken -> jwtToken.getAccessTokenId() == null;
    Predicate<JwtToken> accessTokenIsNull = jwtToken -> jwtTokenIsNull
        .or(accessTokenModelIsNull.or(accessTokenIdIsNull)).test(jwtToken);
    Predicate<JwtToken> validAccessTokenId = jwtToken -> jwtToken.getAccessTokenId()
        .equals(jwtService.getTokenId(token));

    return validateToken(jwtService.getDevice(token), user, jwtToken ->
            (!accessTokenIsNull.and(validAccessTokenId).test(jwtToken)) ? Mono.just(user) : Mono.error(unauthorized()));
  }

  public String genNewRefreshToken(UserRes user) {
    var map = new HashMap<String, String>();
    map.put(IDENTIFIER, UUID.randomUUID().toString());
    map.put(USER_DEVICE, user.getUserDevice());
    return jwtService.genRefreshToken(user, map);
  }

  public String deleteAccessTokensForUser(UserRes user) {
    Predicate<ServerRequest> isTheSameUser = request -> request.pathVariable("id")
        .equals(jwtService.getUserIdClaim(request));

    if (isTheSameUser.test(user.getServerRequest())) {
      String accessTokenId = jwtService.getTokenId(user.getServerRequest());
      Predicate<JwtToken> jwtTokenNotNull = Objects::nonNull;
      Predicate<JwtToken> accessTokenModelNotNull = jwtToken -> jwtToken.getAccessTokenModel() != null;
      Predicate<JwtToken> accessTokenIdMatches = jwtToken -> jwtToken.getAccessTokenId()
              .equals(accessTokenId);
      Predicate<JwtToken> accessTokenIdExists = jwtToken -> jwtTokenNotNull.and(accessTokenModelNotNull)
              .and(accessTokenIdMatches).test(jwtToken);
      removeNullJwtModels(user, accessTokenIdExists);
      return generateNewAccessTokenToUser(user);
    }

    deleteAllAccessTokenModels(user);
    return null;
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
    switch (userRes.getUserDevice()) {
      case ANDROID -> userRes.setAndroidAccessToken(accessTokenModel);
      case IOS -> userRes.setIosAccessToken(accessTokenModel);
      default -> userRes.setWebAccessToken(accessTokenModel);
    }
  }

  public String generateNewAccessTokenToUser(UserRes user) {
    user.setAccessToken(genNewAccessToken(user));
    setUserNewAccToken(user);
    return user.getAccessToken();
  }

  public String genNewAccessToken(UserRes user) {
    HashMap<String, String> map = new HashMap<>();
    map.put(IDENTIFIER, UUID.randomUUID().toString());
    map.put(USER_DEVICE, user.getUserDevice());
    return jwtService.generateAccessToken(user, map);
  }

  public Mono<UserRes> validateToken(String userDevice, UserRes user, Function<JwtToken, Mono<UserRes>> validateToken) {
    return switch (userDevice) {
      case ANDROID -> validateToken.apply(user.getAndroidJwtModel());
      case IOS -> validateToken.apply(user.getIosJwtModel());
      default -> validateToken.apply(user.getWebJwtModel());
    };
  }

  public Mono<Boolean> redisSetUserToken(UserRes userRes, String accessToken) {
    return redisTemplate
            .opsForValue()
            .set(redisTokenK(userRes), jwtService.getTokenId(accessToken), ofMinutes(accessTokenMinValidity));
  }

  public Mono<Boolean> redisRemoveUserToken(UserRes userRes) {
    return redisTemplate.opsForValue().delete(redisTokenK(userRes));
  }


  public void updateUserWithNewTokens(UserRes user) {
    String userIP = user.getServerRequest().remoteAddress().toString().replace("Optional[", "")
        .replace("]", ""); //Remove Optional[*userIPAndPort*]
    RefreshToken reTokenModel = new RefreshToken(jwtService.getTokenId(user.getRefreshToken()), userIP,
        jwtService.getExpDate(user.getRefreshToken()));
    AccessToken accTokenModel = new AccessToken(jwtService.getTokenId(user.getAccessToken()));

    switch (user.getUserDevice()) {
      case ANDROID -> user.setAndroidJwtModel(new AndroidJWT(reTokenModel, accTokenModel));
      case IOS -> user.setIosJwtModel(new IosJWT(reTokenModel, accTokenModel));
      default -> user.setWebJwtModel(new WebJWT(reTokenModel, accTokenModel));
    }
  }

  public void setUserNewAccToken(UserRes user) {
    switch (user.getUserDevice()) {
      case ANDROID -> user.setAndroidAccessToken(new AccessToken(jwtService.getTokenId(user.getAccessToken())));
      case IOS -> user.setIosAccessToken(new AccessToken(jwtService.getTokenId(user.getAccessToken())));
      default -> user.setWebAccessToken(new AccessToken(jwtService.getTokenId(user.getAccessToken())));
    }
  }

  public ResponseCookie refreshHttpCookie(String refreshToken) {
    return switch (jwtService.getDevice(refreshToken)) {
      case ANDROID, IOS -> ResponseCookie.from(REFRESH_TOKEN_KEY, refreshToken)
          .httpOnly(true)
          .maxAge(jwtService.getRefTokenAndroidIosExpDate().getNano())
          //.sameSite("strict")
          //.path("/api/auth/token")
          .build();
      default -> ResponseCookie.from(REFRESH_TOKEN_KEY, refreshToken)
          .httpOnly(true)
          .maxAge(jwtService.getRefTokenWebExpDate().getNano())
          //.sameSite("strict")
          //.path("/api/auth/token")
          .build();
    };
  }

  public ResponseCookie removeRefreshHttpCookie() {
    return ResponseCookie.from(REFRESH_TOKEN_KEY, "")
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

    if (request.headers().header("Cookie").size() == 0) { unauthorizedRefCookie.get(); }
    final String[] refToken = {""};

    Arrays.stream(request.headers().header("Cookie").get(0).split(";"))
        .filter(s -> s.contains(REFRESH_TOKEN_KEY))
        .findFirst()
        .map(s -> s.replace(REFRESH_TOKEN_KEY + "=", ""))
        .ifPresentOrElse(s -> refToken[0] = s, unauthorizedRefCookie::get);
    jwtService.getUserIdClaim(refToken[0]);
    return refToken[0];
  }


  public LocalDateTime refreshTokenExp (UserRes userRes) {
    return LocalDate.from(
                    jwtService
                            .getExpDate(getRefreshToken(userRes.getServerRequest()))
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .atStartOfDay();
  }

  public boolean isRefreshAboutToExp(UserRes userRes) {
    LocalDateTime day100FromNow = LocalDate.now().plusDays(JWT_REFRESH_DAYS).atStartOfDay();
    IntPredicate refreshIsAboutToExpire = jwtRefreshDays ->
                    Duration.between(day100FromNow, refreshTokenExp(userRes)).toDays() < jwtRefreshDays;
    return refreshIsAboutToExpire.test(JWT_REFRESH_DAYS);
  }

  public void updateAccTokenOrTokens(UserRes userRes) {
    if (isRefreshAboutToExp(userRes)) {
      updateUserWithNewTokens(userRes);
    } else {
      setUserNewAccToken(userRes);
    }
  }

  public String refTokenFromHttpCookie(UserRes user) {
    return Objects.requireNonNull(user.getServerRequest().cookies().getFirst(REFRESH_TOKEN_KEY))
            .getValue();
  }
}
