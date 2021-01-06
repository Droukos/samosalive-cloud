package com.droukos.osmservice.environment.security.tokens;

import com.droukos.osmservice.config.jwt.AccessTokenConfig;
import com.droukos.osmservice.config.jwt.ClaimsConfig;
import com.droukos.osmservice.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.droukos.osmservice.util.factories.HttpExceptionFactory.unauthorized;

@Service
@RequiredArgsConstructor
public class AccessJwtService {

    @NonNull
    private final ReactiveStringRedisTemplate redisTemplate;
    @NonNull
    private final ClaimsConfig claimsConfig;
    @NonNull
    private final AccessTokenConfig accessTokenConfig;

    private String getAuthHeader(ServerRequest request) {
        return request
                .headers()
                .header("Authorization")
                .get(0)
                .replace(accessTokenConfig.getTokenPrefix(), "")
                .trim();
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
                        value ->
                                (value.isEmpty() || !value.equals(tokenId))
                                        ? Mono.error(unauthorized())
                                        : Mono.just(claimsJws.getBody()));
    }
}
