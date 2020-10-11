package com.droukos.userservice;

import com.droukos.userservice.config.jwt.AccessTokenConfig;
import com.droukos.userservice.config.jwt.ClaimsConfig;
import com.droukos.userservice.util.DateUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.time.LocalDateTime.now;

public class TokenUtilTest {
    private TokenUtilTest() {}

    public static final MimeType mimeType = new MediaType("message", "x.rsocket.authentication.bearer.v0");
    public static String accessToken ;
    public static final String userId = "5f7bd2d847706533411e5e55";
    public static final String username = "Ko";
    public static final List<String> roles = Collections.singletonList("GENERAL_ADMIN");
    public static final String tokenId = "randomId";
    public static final String platform = "platform";

    public static String tokenGenerator(ClaimsConfig claimsConfig, AccessTokenConfig accessTokenConfig) {

        return Jwts.builder()
                .setSubject(username)
                .claim(claimsConfig.getUserId(), userId)
                .claim(claimsConfig.getAuthorities(), roles)
                .claim(claimsConfig.getTokenId(), tokenId)
                .claim(claimsConfig.getPlatform(), platform)
                .signWith(SignatureAlgorithm.HS256, accessTokenConfig.getSecretKey())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(DateUtils.asDate(now().plusMinutes(accessTokenConfig.getValidMinutesAll())))
                .compact();
    }
}
