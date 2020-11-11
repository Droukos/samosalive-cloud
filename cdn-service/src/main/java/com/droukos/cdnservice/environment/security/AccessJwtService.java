package com.droukos.cdnservice.environment.security;

import com.droukos.cdnservice.config.jwt.AccessTokenConfig;
import com.droukos.cdnservice.config.jwt.ClaimsConfig;
import com.droukos.cdnservice.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.droukos.cdnservice.environment.security.HttpExceptionFactory.unauthorized;

@Service
@RequiredArgsConstructor
public class AccessJwtService {

  @NonNull private final ReactiveStringRedisTemplate redisTemplate;
  @NonNull private final AccessTokenConfig accessTokenConfig;
  @NonNull private final ClaimsConfig claimsConfig;

  public Mono<Claims> testToken(String token) {
    return Mono.just(Jwts.parser().setSigningKey(accessTokenConfig.getSecretKey()))
        .flatMap(jwtParser -> Mono.just(jwtParser.parseClaimsJws(token)))
        .onErrorResume(Exception.class, err -> Mono.error(unauthorized("expired token")))
        .flatMap(this::isTokenAuthenticated);
  }

  public Mono<Claims> isTokenAuthenticated(Jws<Claims> claimsJws) {
    Claims tokenClaims = claimsJws.getBody();
    String userId = tokenClaims.get(claimsConfig.getUserId(), String.class);
    String userDevice = tokenClaims.get(claimsConfig.getPlatform(), String.class);
    String tokenId = tokenClaims.get(claimsConfig.getTokenId(), String.class);

    return redisTemplate
        .opsForValue()
        .get(RedisUtil.redisTokenK(userId, userDevice))
        .switchIfEmpty(Mono.just(""))
        .flatMap(
            value ->
                (value.isEmpty() || !value.equals(tokenId))
                    ? Mono.error(unauthorized())
                    : Mono.just(claimsJws.getBody()));
  }

  public Mono<Claims> getAllClaims(String token) {
    return Mono.just(
            Jwts.parser().setSigningKey(accessTokenConfig.getSecretKey()).parseClaimsJws(token).getBody());
  }

  private Mono<String> userAccToken(SecurityContext securityContext) {
    return Mono.just(securityContext.getAuthentication().getPrincipal().toString());
  }
}
