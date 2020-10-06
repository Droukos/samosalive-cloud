package com.droukos.authservice.environment.security.tokens;

import com.droukos.authservice.config.jwt.ClaimsConfig;
import com.droukos.authservice.config.jwt.RefreshTokenConfig;
import com.droukos.authservice.environment.dto.RequesterAccessTokenData;
import com.droukos.authservice.environment.dto.RequesterRefreshTokenData;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.util.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import static com.droukos.authservice.environment.constants.Platforms.ANDROID;
import static com.droukos.authservice.environment.constants.Platforms.IOS;

@Service
@RequiredArgsConstructor
public class RefreshJwtService {

  @NonNull private final ClaimsConfig claimsConfig;
  @NonNull private final RefreshTokenConfig refreshTokenConfig;

  public String getUserIdClaim(String token) {
    return getField(token, claimsConfig.getUserId());
  }

  private String getField(String token, String field) {
    return getAllClaims(token).get(field, String.class);
  }

  public Claims getAllClaims(String token) {
    return Jwts.parser()
        .setSigningKey(refreshTokenConfig.getSecretKey())
        .parseClaimsJws(token)
        .getBody();
  }

  public Mono<Claims> getAllClaimsMono(String token) {
    return Mono.just(getAllClaims(token));
  }

  public Mono<UserRes> requesterDataDtoFromToken(UserRes user) {
    return Mono.just(user.getRequesterRefreshTokenData().getToken())
            .flatMap(this::getAllClaimsMono)
            .flatMap(this::populateRefreshTokenUserData)
            .doOnNext(user::setRequesterRefreshTokenData)
            .doOnNext(requesterRefreshTokenData -> user.setRequesterAccessTokenData(
                    RequesterAccessTokenData.builder()
                            .userDevice(requesterRefreshTokenData.getUserDevice())
                            .build()))
            .then(Mono.just(user));
  }

  public RequesterRefreshTokenData genRefreshToken(UserRes user, Map<String, String> issueInfo) {
    String userPlatform = issueInfo.get(claimsConfig.getPlatform());
    return userPlatform.equals(ANDROID) || userPlatform.equals(IOS)
        ? tokenGenerator(user, issueInfo, getRefTokenAndroidIosExpDate())
        : tokenGenerator(user, issueInfo, getRefTokenWebExpDate());
  }

  public LocalDateTime getRefTokenAndroidIosExpDate() {
    return LocalDateTime.now().plusDays(refreshTokenConfig.getValidDaysNative());
  }

  public LocalDateTime getRefTokenWebExpDate() {
    return LocalDateTime.now().plusDays(refreshTokenConfig.getValidDaysWeb());
  }

  private Mono<RequesterRefreshTokenData> populateRefreshTokenUserData(Claims claims) {
    return Mono.just(
            RequesterRefreshTokenData.builder()
                    .userId(claims.get(claimsConfig.getUserId(), String.class))
                    .username(claims.getSubject())
                    .tokenId(claims.get(claimsConfig.getTokenId(), String.class))
                    .expiration(claims.getExpiration())
                    .userDevice(claims.get(claimsConfig.getPlatform(), String.class))
                    .build());
  }

  private RequesterRefreshTokenData tokenGenerator(
      UserRes user, Map<String, String> issueInfo, LocalDateTime validityDate) {

    String tokenId = issueInfo.get(claimsConfig.getTokenId());
    String platform = issueInfo.get(claimsConfig.getPlatform());
    Date dateToExpire = DateUtils.asDate(validityDate);

    String refToken = Jwts.builder()
        .setSubject(user.getUser())
        .claim(claimsConfig.getUserId(), user.getId())
        .claim(claimsConfig.getTokenId(), tokenId)
        .claim(claimsConfig.getPlatform(), platform)
        .signWith(SignatureAlgorithm.HS256, refreshTokenConfig.getSecretKey())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(dateToExpire)
        .compact();

    return RequesterRefreshTokenData.builder()
            .token(refToken)
            .tokenId(tokenId)
            .userId(user.getId())
            .userDevice(platform)
            .expiration(dateToExpire)
            .build();
  }
}
