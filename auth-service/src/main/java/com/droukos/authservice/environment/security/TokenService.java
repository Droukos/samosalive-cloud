package com.droukos.authservice.environment.security;

import com.droukos.authservice.config.jwt.AccessTokenConfig;
import com.droukos.authservice.config.jwt.ClaimsConfig;
import com.droukos.authservice.config.jwt.RefreshTokenConfig;
import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.environment.dto.RequesterAccessTokenData;
import com.droukos.authservice.environment.dto.RequesterRefTokenData;
import com.droukos.authservice.environment.interfaces.JwtToken;
import com.droukos.authservice.environment.security.tokens.AccessJwtService;
import com.droukos.authservice.environment.security.tokens.RefreshJwtService;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.util.SecurityUtil;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.*;

import static com.droukos.authservice.environment.constants.Platforms.*;
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

  public Mono<UserRes> validateRefreshToken(Tuple2<UserRes, RequesterRefTokenData> tuple2) {

    return validateToken(tuple2.getT2().getUserDevice(), tuple2.getT1(), jwtToken ->
            !(jwtToken == null || jwtToken.getRefreshTokenId() == null)
                    && jwtToken.getRefreshTokenId().equals(tuple2.getT2().getTokenId())
                    ? Mono.just(tuple2.getT1())
                    : Mono.error(unauthorized()));
  }

  public Mono<UserRes> validateToken(String userDevice, UserRes user, Function<JwtToken, Mono<UserRes>> validateToken) {

    return switch (userDevice) {
      case ANDROID -> validateToken.apply(user.getAndroidJwtModel());
      case IOS -> validateToken.apply(user.getIosJwtModel());
      default -> validateToken.apply(user.getWebJwtModel());
    };
  }

  public RequesterRefTokenData getRequesterRefreshData(ServerRequest request) {

    String refToken = getRefreshToken(request);
    Claims claims = refreshJwtService.getAllClaims(refToken);
    return new RequesterRefTokenData(
            refToken,
            claims.get(claimsConfig.getTokenId(), String.class),
            claims.get(claimsConfig.getUserId(), String.class),
            claims.getSubject(),
            claims.get(claimsConfig.getPlatform(), String.class),
            claims.getExpiration()
    );
  }

  public Mono<NewAccTokenData> genNewAccessToken(UserRes user) {

    return ReactiveSecurityContextHolder.getContext()
            .flatMap(context -> {
              RequesterAccessTokenData tokenData =((RequesterAccessTokenData) context.getAuthentication().getPrincipal());
              HashMap<String, String> map = new HashMap<>();
              map.put(claimsConfig.getTokenId(), UUID.randomUUID().toString());
              map.put(claimsConfig.getPlatform(), tokenData.getUserDevice());
              return accessJwtService.generateAccessToken(user, map);
            });
  }

  public Mono<NewAccTokenData> genNewAccessToken(UserRes user, String os) {

    HashMap<String, String> map = new HashMap<>();
    map.put(claimsConfig.getTokenId(), UUID.randomUUID().toString());
    map.put(claimsConfig.getPlatform(), os);
    return accessJwtService.generateAccessToken(user, map);
  }

  public Mono<NewRefTokenData> genNewRefreshToken(UserRes user) {

    return ReactiveSecurityContextHolder.getContext()
            .flatMap(context -> {
              var map = new HashMap<String, String>();
              map.put(claimsConfig.getTokenId(), UUID.randomUUID().toString());
              map.put(claimsConfig.getPlatform(), SecurityUtil.getRequesterDevice(context));
              return refreshJwtService.genRefreshToken(user, map);
            });
  }

  public Mono<NewRefTokenData> genNewRefreshToken(UserRes user, String os) {

    var map = new HashMap<String, String>();
    map.put(claimsConfig.getTokenId(), UUID.randomUUID().toString());
    map.put(claimsConfig.getPlatform(), os);
    return refreshJwtService.genRefreshToken(user, map);
  }

  public Mono<Boolean> redisSetUserToken(UserRes user, NewAccTokenData accessTokenData) {
    return redisTemplate
            .opsForValue()
            .set(redisTokenK(user, accessTokenData),
                    accessTokenData.getTokenId(),
                    ofMinutes(accessTokenConfig.getValidMinutesAll()));
  }

  public Mono<Boolean> redisSetUserToken(UserRes user, String accTokenId, String platform) {
    return redisTemplate
            .opsForValue()
            .set(redisTokenK(user, platform), accTokenId, ofMinutes(accessTokenConfig.getValidMinutesAll()));
  }

  public Mono<Boolean> redisRemoveUserToken(UserRes userRes, String device) {
    return redisTemplate.opsForValue().delete(redisTokenK(userRes, device));
  }

  public Mono<Boolean> redisRemoveAllUserAccessTokens(UserRes userRes) {
    return redisTemplate.opsForValue().delete(redisTokenK(userRes, ANDROID))
            .then(redisTemplate.opsForValue().delete(redisTokenK(userRes, IOS)))
            .then(redisTemplate.opsForValue().delete(redisTokenK(userRes, WEB)));
  }

  public ResponseCookie refreshHttpCookie(NewRefTokenData newRefTokenData) {

    return switch (newRefTokenData.getUserDevice()) {
      case ANDROID, IOS ->
              ResponseCookie.from(refreshTokenConfig.getCookieName(), newRefTokenData.getToken())
                      .httpOnly(true)
                      .maxAge(refreshJwtService.getRefTokenAndroidIosExpDate().getNano())
                      //.sameSite("strict")
                      // .path("/api/auth/token")
                      .build();
      default ->
              ResponseCookie.from(refreshTokenConfig.getCookieName(), newRefTokenData.getToken())
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


  public LocalDateTime refreshTokenExp (RequesterRefTokenData refreshTokenData) {
    return LocalDate.from(
            refreshTokenData
                    .getExpiration()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate())
            .atStartOfDay();
  }

  public boolean isRefreshAboutToExp(RequesterRefTokenData refreshTokenData) {
    LocalDateTime day100FromNow = LocalDate.now().plusDays(refreshTokenConfig.getRenewDaysAll()).atStartOfDay();
    IntPredicate refreshIsAboutToExpire = jwtRefreshDays ->
                    Duration.between(day100FromNow, refreshTokenExp(refreshTokenData)).toDays() < jwtRefreshDays;
    return refreshIsAboutToExpire.test(refreshTokenConfig.getRenewDaysAll());
  }
}
