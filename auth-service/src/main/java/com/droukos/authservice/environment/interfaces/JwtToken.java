package com.droukos.authservice.environment.interfaces;

import com.droukos.authservice.model.user.system.security.jwt.tokens.AccessToken;
import com.droukos.authservice.model.user.system.security.jwt.tokens.RefreshToken;

import java.util.Date;

public interface JwtToken {
    AccessToken getAccessTokenModel();
    RefreshToken getRefreshTokenModel();
    String getAccessTokenId();
    String getRefreshTokenId();
    Date getRefreshTokenExp();
}
