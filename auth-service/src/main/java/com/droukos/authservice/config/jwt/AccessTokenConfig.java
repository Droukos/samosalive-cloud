package com.droukos.authservice.config.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AccessTokenConfig {
    @Value("${jwt.prefix.bearer}")
    private String tokenPrefix;

    @Value("${jwt.token.access.secret-key}")
    private String secretKey;

    @Value("${jwt.token.access.validity.all.minutes}")
    private long validMinutesAll;
}
