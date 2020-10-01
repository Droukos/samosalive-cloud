package com.droukos.authservice.environment.security;

import com.droukos.authservice.model.user.Role;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.util.DateUtils;
import com.droukos.authservice.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.droukos.authservice.environment.constants.Platforms.ANDROID;
import static com.droukos.authservice.environment.constants.Platforms.IOS;
import static com.droukos.authservice.environment.security.HttpExceptionFactory.unauthorized;

@Service
@RequiredArgsConstructor
public class JwtService {

  @NonNull private final ReactiveStringRedisTemplate redisTemplate;

  @Value("${jwt.user_id}")
  public String userIdClaim;

  @Value("${jwt.authorities_key}")
  public String authoritiesKey;

  @Value("${jwt.identifier}")
  public String identifier;

  @Value("${user.platform}")
  public String userDeviceClaim;

  @Value("${jwt.validity_hour}")
  public Long validityHour;

  @Value("${jwt.signing_key}")
  public String signingKey;

  @Value("${jwt.prefix.bearer}")
  public String tokenPrefix;

  @Value("${jwt.refresh_token_validity_fornative}")
  public String refreshTokenValidityForNative;

  @Value("${jwt.refresh_token_validity_forweb}")
  public String refreshTokenValidityForWeb;

  @Value("${jwt.access_token_validity}")
  public String accessTokenValidity;

  public String getUsername(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public String getUsername(ServerRequest request) {
    return getUsername(getAuthHeader(request));
  }

  public String getUserIdClaim(ServerRequest request) {
    return getField(request, userIdClaim);
  }

  public String getUserIdClaim(String token) {
    return getField(token, userIdClaim);
  }

  @SuppressWarnings("unchecked")
  public List<String> getRoles(ServerRequest request) {
    return getAllClaims(getAuthHeader(request)).get(authoritiesKey, ArrayList.class);
  }

  @SuppressWarnings("unchecked")
  public List<String> getRoles(String token) {
    return getAllClaims(token).get(authoritiesKey, ArrayList.class);
  }

  public String getTokenId(ServerRequest request) {
    return getField(request, identifier);
  }

  public String getTokenId(String token) {
    return getField(token, identifier);
  }

  public String getDevice(ServerRequest request) {
    return getField(request, userDeviceClaim);
  }

  public String getDevice(String token) {
    return getField(token, userDeviceClaim);
  }

  private String getField(ServerRequest request, String field) {
    return getAllClaims(getAuthHeader(request)).get(field, String.class);
  }

  private String getField(String token, String field) {
    return getAllClaims(token.replace(tokenPrefix, "").trim()).get(field, String.class);
  }

  public Date getExpDate(ServerRequest request) {
    return getExpDate(getAuthHeader(request));
  }

  public Date getExpDate(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    return claimsResolver.apply(getAllClaims(token));
  }

  public Claims getAllClaims(String token) {
    return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();
  }

  public Mono<Claims> fetchClaims(AuthorizationContext context) {
    return Mono.just(
            Objects.requireNonNull(
                    context
                        .getExchange()
                        .getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION))
                .replace(tokenPrefix, "")
                .trim())
        .flatMap(this::testToken);
  }

  public Mono<Claims> testToken(String token) {
    return Mono.just(Jwts.parser().setSigningKey(signingKey))
        .flatMap(jwtParser -> Mono.just(jwtParser.parseClaimsJws(token)))
        .onErrorResume(Exception.class, err -> Mono.error(unauthorized("expired token")))
        .flatMap(claimsJws -> isTokenAuthenticated(token, claimsJws));
  }

  public Mono<Claims> isTokenAuthenticated(String token, Jws<Claims> claimsJws) {
    String userId = getUserIdClaim(token);
    String userDevice = getDevice(token);

    return redisTemplate
        .opsForValue()
        .get(RedisUtil.redisTokenK(userId, userDevice))
        .switchIfEmpty(Mono.just(""))
        .flatMap(
            value ->
                (value.isEmpty() || !value.equals(getTokenId(token)))
                    ? Mono.error(unauthorized())
                    : Mono.just(claimsJws.getBody()));
  }

  public Boolean isTokenExpired(String token) {
    return getExpDate(token).before(new Date(System.currentTimeMillis()));
  }

  public String generateAccessToken(UserRes user, Map<String, String> issueInfo) {
    return tokenGenerator(
        user, issueInfo, LocalDateTime.now().plusMinutes(Integer.parseInt(accessTokenValidity)));
  }

  public LocalDateTime getRefTokenAndroidIosExpDate() {
    return LocalDateTime.now().plusDays(Integer.parseInt(refreshTokenValidityForWeb));
  }

  public LocalDateTime getRefTokenWebExpDate() {
    return LocalDateTime.now().plusDays(Integer.parseInt(refreshTokenValidityForNative));
  }

  public String genRefreshToken(UserRes user, Map<String, String> issueInfo) {
    String userPlatform = issueInfo.get(userDeviceClaim);
    return userPlatform.equals(ANDROID) || userPlatform.equals(IOS)
        ? tokenGenerator(user, issueInfo, getRefTokenAndroidIosExpDate())
        : tokenGenerator(user, issueInfo, getRefTokenWebExpDate());
  }

  private String getAuthHeader(ServerRequest request) {
    return request.headers().header("Authorization").get(0).replace(tokenPrefix, "").trim();
  }

  private String tokenGenerator(
      UserRes user, Map<String, String> issueInfo, LocalDateTime validityDate) {
    return Jwts.builder()
        .setSubject(user.getUser())
        .claim(userIdClaim, user.getId())
        .claim(
            authoritiesKey,
            user.getAllRoles().stream().map(Role::getCode).collect(Collectors.toList()))
        .claim(identifier, issueInfo.get(identifier))
        .claim(userDeviceClaim, issueInfo.get(userDeviceClaim))
        .signWith(SignatureAlgorithm.HS256, signingKey)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(DateUtils.asDate(validityDate))
        .compact();
  }

  public void setUserDeviceToUserDto(UserRes userRes) {
    userRes.setUserDevice(getDevice(userRes.getServerRequest()));
  }
}
