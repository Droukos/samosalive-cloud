package com.droukos.authservice.config.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ClaimsConfig {
    @Value("${jwt.claims.userId}")
    private String userId;
    @Value("${jwt.claims.authorities}")
    private String authorities;
    @Value("${jwt.claims.tokenId}")
    private String tokenId;
    @Value("${jwt.claims.platform}")
    private String platform;
}
