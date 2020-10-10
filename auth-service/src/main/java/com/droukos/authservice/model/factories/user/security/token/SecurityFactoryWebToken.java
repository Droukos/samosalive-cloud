package com.droukos.authservice.model.factories.user.security.token;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.security.Security;
import com.droukos.authservice.model.user.system.security.jwt.platforms.AndroidJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.IosJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.WebJWT;

public class SecurityFactoryWebToken {
    private SecurityFactoryWebToken() {}

    public static Security webUpdateAccessToken (UserRes user, NewAccTokenData tokenData) {

        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                user.getAndroidJwtModel(),
                user.getIosJwtModel(),
                WebJWT.jwtUpdateAccessToken(user, tokenData),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountBannedModel()
        );
    }

    public static Security webUpdateOnlyTokens
            (UserRes user, NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {

        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                user.getAndroidJwtModel(),
                user.getIosJwtModel(),
                WebJWT.jwtUpdate(accessTokenData, refreshTokenData),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountBannedModel()
        );
    }

    public static Security webDeleteAccessToken (UserRes user) {

        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                user.getAndroidJwtModel(),
                user.getIosJwtModel(),
                WebJWT.jwtDeleteAccessToken(user),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountBannedModel()
        );
    }

    public static Security webUpdateAccessTokenDeleteOtherAccessTokens (UserRes user, NewAccTokenData tokenData) {

        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                AndroidJWT.jwtDeleteAccessToken(user),
                IosJWT.jwtDeleteAccessToken(user),
                WebJWT.jwtUpdateAccessToken(user, tokenData),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountBannedModel()
        );
    }

    public static Security webUpdateTokensDeleteOtherTokens
            (UserRes user, NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {

        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                AndroidJWT.jwtDeleteTokens(),
                IosJWT.jwtDeleteTokens(),
                WebJWT.jwtUpdate(accessTokenData, refreshTokenData),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountBannedModel());
    }
}
