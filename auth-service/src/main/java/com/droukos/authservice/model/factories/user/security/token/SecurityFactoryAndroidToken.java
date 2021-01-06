package com.droukos.authservice.model.factories.user.security.token;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.security.Security;
import com.droukos.authservice.model.user.system.security.jwt.platforms.AndroidJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.IosJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.WebJWT;

public class SecurityFactoryAndroidToken {
    private SecurityFactoryAndroidToken() {}

    public static Security androidUpdateAccessToken (UserRes user, NewAccTokenData tokenData) {

        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                AndroidJWT.jwtUpdateAccessToken(user, tokenData),
                user.getIosJwtModel(),
                user.getWebJwtModel(),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountStatusModel()
        );
    }

    public static Security androidUpdateOnlyTokens
            (UserRes user, NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {

        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                AndroidJWT.jwtUpdate(accessTokenData, refreshTokenData),
                user.getIosJwtModel(),
                user.getWebJwtModel(),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountStatusModel()
        );
    }

    public static Security androidDeleteAccessToken (UserRes user) {

        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                AndroidJWT.jwtDeleteAccessToken(user),
                user.getIosJwtModel(),
                user.getWebJwtModel(),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountStatusModel()
        );
    }

    public static Security androidUpdateAccessTokenDeleteOtherAccessTokens (UserRes user, NewAccTokenData tokenData) {

        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                AndroidJWT.jwtUpdateAccessToken(user, tokenData),
                IosJWT.jwtDeleteAccessToken(user),
                WebJWT.jwtDeleteAccessToken(user),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountStatusModel()
        );
    }

    public static Security androidUpdateTokensDeleteOtherTokens
            (UserRes user, NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {

        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                AndroidJWT.jwtUpdate(accessTokenData, refreshTokenData),
                IosJWT.jwtDeleteTokens(),
                WebJWT.jwtDeleteTokens(),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountStatusModel());
    }
}
