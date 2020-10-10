package com.droukos.authservice.environment.security.tokens;

import com.droukos.authservice.config.jwt.ClaimsConfig;
import com.droukos.authservice.config.jwt.RefreshTokenConfig;
import com.droukos.authservice.environment.dto.NewRefTokenData;
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

  public Mono<NewRefTokenData> genRefreshToken(UserRes user, Map<String, String> issueInfo) {
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

  private Mono<NewRefTokenData> tokenGenerator(
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

    return Mono.just(
            new NewRefTokenData(
                    refToken,
                    tokenId,
                    null,
                    user.getId(),
                    user.getUser(),
                    platform,
                    dateToExpire)
           );
  }
}
