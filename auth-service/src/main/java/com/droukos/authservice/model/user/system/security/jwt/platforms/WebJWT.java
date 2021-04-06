package com.droukos.authservice.model.user.system.security.jwt.platforms;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.environment.interfaces.JwtToken;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.security.jwt.tokens.AccessToken;
import com.droukos.authservice.model.user.system.security.jwt.tokens.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class WebJWT implements JwtToken {
    RefreshToken reToken;
    AccessToken accToken;

    @Override
    public AccessToken getAccessTokenModel() {
        return accToken;
    }

    @Override
    public RefreshToken getRefreshTokenModel() {
        return reToken;
    }

    @Override
    public String getAccessTokenId() {
        return accToken.getAid();
    }

    @Override
    public String getRefreshTokenId() {
        return reToken.getRid();
    }

    @Override
    public Date getRefreshTokenExp() {
        return reToken.getExp();
    }

    public static WebJWT noJwtUpdate(UserRes user) {
        return new WebJWT(
                RefreshToken.noUpdate(user.getSys().getSec().getWebJWT().reToken),
                AccessToken.noUpdate(user.getSys().getSec().getWebJWT().accToken)
        );
    }

    public static WebJWT jwtAccessTokenUpdate(UserRes user, NewAccTokenData accTokenData) {
        return new WebJWT(user.getWebJwtModel().getReToken(), AccessToken.update(accTokenData));
    }

    public static WebJWT jwtUpdate(NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {
        return new WebJWT(
                RefreshToken.update(refreshTokenData),
                AccessToken.update(accessTokenData)
        );
    }

    public static WebJWT jwtDeleteAccessToken(UserRes user) {
        return user.getWebJwtModel() != null && user.getWebJwtModel().getReToken() != null
                ? new WebJWT(user.getWebJwtModel().getReToken(), null)
                : null;
    }

    public static WebJWT jwtDeleteTokens() {
        return null;
    }

    public static WebJWT jwtUpdateAccessToken(UserRes user, NewAccTokenData tokenData) {
        return new WebJWT(user.getWebJwtModel().getReToken(), AccessToken.update(tokenData));
    }
}
