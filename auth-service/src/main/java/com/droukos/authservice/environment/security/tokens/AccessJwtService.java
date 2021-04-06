package com.droukos.authservice.environment.security.tokens;

import com.droukos.authservice.config.jwt.AccessTokenConfig;
import com.droukos.authservice.config.jwt.ClaimsConfig;
import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.model.user.RoleModel;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.util.DateUtils;
import com.droukos.authservice.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.droukos.authservice.util.factories.HttpExceptionFactory.unauthorized;
import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
public class AccessJwtService {

  private final ReactiveStringRedisTemplate redisTemplate;
  private final ClaimsConfig claimsConfig;
  private final AccessTokenConfig accessTokenConfig;

  public Claims getAllClaims(String token) {
    return Jwts.parser()
        .setSigningKey(accessTokenConfig.getSecretKey())
        .parseClaimsJws(token)
        .getBody();
  }

  public Mono<Claims> testToken(String token) {
    return Mono.just(Jwts.parser().setSigningKey(accessTokenConfig.getSecretKey()))
        .flatMap(jwtParser -> Mono.just(jwtParser.parseClaimsJws(token)))
        .onErrorResume(Exception.class, err -> Mono.error(unauthorized("expired token")))
        .flatMap(this::isTokenAuthenticated);
  }

  public Mono<Claims> isTokenAuthenticated(Jws<Claims> claimsJws) {
    Claims claims = claimsJws.getBody();
    String userId = claims.get(claimsConfig.getUserId(), String.class);
    String userDevice = claims.get(claimsConfig.getPlatform(), String.class);
    String tokenId = claims.get(claimsConfig.getTokenId(), String.class);

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

  public Mono<NewAccTokenData> generateAccessToken(UserRes user, Map<String, String> issueInfo) {

    return tokenGenerator(
        user, issueInfo, now().plusMinutes(accessTokenConfig.getValidMinutesAll()));
  }

  private Mono<NewAccTokenData> tokenGenerator(
      UserRes user, Map<String, String> issueInfo, LocalDateTime validityDate) {

    List<String> roles = user.getAllRoles().stream().map(RoleModel::getRole).collect(Collectors.toList());
    String tokenId = issueInfo.get(claimsConfig.getTokenId());
    String platform = issueInfo.get(claimsConfig.getPlatform());
    Date dateToExpire = DateUtils.asDate(validityDate);

    String accessToken =
        Jwts.builder()
            .setSubject(user.getUser())
            .claim(claimsConfig.getUserId(), user.getId())
            .claim(claimsConfig.getAuthorities(), roles)
            .claim(claimsConfig.getTokenId(), tokenId)
            .claim(claimsConfig.getPlatform(), platform)
            .signWith(SignatureAlgorithm.HS256, accessTokenConfig.getSecretKey())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(dateToExpire)
            .compact();

    return Mono.just(
            new NewAccTokenData(accessToken, tokenId, user.getId(), user.getUser(), platform, dateToExpire, roles));
  }
}
