package com.droukos.cdnservice.config.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class RefreshTokenConfig {
    @Value("${jwt.token.refresh.secret-key}")
    private String secretKey;

    @Value("${jwt.token.refresh.validity.native.days}")
    private long validDaysNative;

    @Value("${jwt.token.refresh.validity.web.days}")
    private long validDaysWeb;

    @Value("${jwt.token.refresh.renew.all.days}")
    private int renewDaysAll;

    @Value("${jwt.token.refresh.cookie.name}")
    private String cookieName;
}
