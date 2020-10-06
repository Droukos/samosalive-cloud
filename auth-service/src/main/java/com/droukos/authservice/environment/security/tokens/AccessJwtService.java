package com.droukos.authservice.environment.security.tokens;

import com.droukos.authservice.config.jwt.AccessTokenConfig;
import com.droukos.authservice.config.jwt.ClaimsConfig;
import com.droukos.authservice.environment.dto.RequesterAccessTokenData;
import com.droukos.authservice.model.user.RoleModel;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.util.DateUtils;
import com.droukos.authservice.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.droukos.authservice.util.factories.HttpExceptionFactory.unauthorized;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class AccessJwtService {

  @NonNull private final ReactiveStringRedisTemplate redisTemplate;
  @NonNull private final ClaimsConfig claimsConfig;
  @NonNull private final AccessTokenConfig accessTokenConfig;

  private String getAuthHeader(ServerRequest request) {
    return request
        .headers()
        .header("Authorization")
        .get(0)
        .replace(accessTokenConfig.getTokenPrefix(), "")
        .trim();
  }

  public Mono<UserRes> requesterDataDtoFromRequest(UserRes user) {
    return Mono.just(getAuthHeader(user.getServerRequest()))
        .flatMap(this::getAllClaimsMono)
        .flatMap(this::populateAccessTokenUserDataMono)
        .doOnNext(user::setRequesterAccessTokenData)
        .then(Mono.just(user));
  }

  public Mono<Claims> getAllClaimsMono(String token) {
    return Mono.just(getAllClaims(token));
  }

  public String getUserIdClaim(ServerRequest request) {
    return getField(getAuthHeader(request), claimsConfig.getUserId());
  }

  public String getUserIdClaim(String token) {
    return getField(token, claimsConfig.getUserId());
  }

  private String getField(String token, String field) {
    return getAllClaims(token.replace(accessTokenConfig.getTokenPrefix(), "").trim())
        .get(field, String.class);
  }

  public Mono<Claims> fetchClaims(AuthorizationContext context) {
    return Mono.just(
            Objects.requireNonNull(
                    context
                        .getExchange()
                        .getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION))
                .replace(accessTokenConfig.getTokenPrefix(), "")
                .trim())
        .onErrorResume(Exception.class, err -> Mono.error(unauthorized()))
        .flatMap(this::testToken);
  }

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
            value -> (value.isEmpty() || !value.equals(tokenId))
                    ? Mono.error(unauthorized())
                    : Mono.just(claimsJws.getBody()));
  }

  @SuppressWarnings("unchecked")
  private RequesterAccessTokenData populateAccessTokenUserData(Claims claims) {
    return RequesterAccessTokenData.builder()
        .userId(claims.get(claimsConfig.getUserId(), String.class))
        .username(claims.getSubject())
        .tokenId(claims.get(claimsConfig.getTokenId(), String.class))
        .roles(claims.get(claimsConfig.getAuthorities(), List.class))
        .userDevice(claims.get(claimsConfig.getPlatform(), String.class))
        .build();
  }

  private Mono<RequesterAccessTokenData> populateAccessTokenUserDataMono(Claims claims) {
    return Mono.just(populateAccessTokenUserData(claims));
  }

  public RequesterAccessTokenData generateAccessToken(
      UserRes user, Map<String, String> issueInfo) {

    return tokenGenerator(
        user, issueInfo, now().plusMinutes(accessTokenConfig.getValidMinutesAll()));
  }

  private RequesterAccessTokenData tokenGenerator(
      UserRes user, Map<String, String> issueInfo, LocalDateTime validityDate) {

    List<String> roles =
        user.getAllRoles().stream().map(RoleModel::getRole).collect(Collectors.toList());
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

    return RequesterAccessTokenData.builder()
                .token(accessToken)
                .tokenId(tokenId)
                .userId(user.getId())
                .userDevice(platform)
                .roles(roles)
                .expiration(dateToExpire)
                .build();
  }
}
