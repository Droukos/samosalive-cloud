package com.droukos.userservice.environment.interfaces;

import com.droukos.userservice.model.user.system.security.jwt.tokens.AccessToken;
import com.droukos.userservice.model.user.system.security.jwt.tokens.RefreshToken;

import java.util.Date;

public interface JwtToken {
    AccessToken getAccessTokenModel();
    RefreshToken getRefreshTokenModel();
    String getAccessTokenId();
    String getRefreshTokenId();
    Date getRefreshTokenExp();
}
