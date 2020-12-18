package com.droukos.authservice.model.factories.user.security.token;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.security.Security;
import com.droukos.authservice.model.user.system.security.jwt.platforms.AndroidJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.IosJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.WebJWT;

public class SecurityFactoryIosToken {
    private SecurityFactoryIosToken() {}

    public static Security iosUpdateAccessToken (UserRes user, NewAccTokenData tokenData) {

        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                user.getAndroidJwtModel(),
                IosJWT.jwtUpdateAccessToken(user, tokenData),
                user.getWebJwtModel(),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountStatusModel()
        );
    }

    public static Security iosUpdateOnlyTokens
            (UserRes user, NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {

        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                user.getAndroidJwtModel(),
                IosJWT.jwtUpdate(accessTokenData, refreshTokenData),
                user.getWebJwtModel(),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountStatusModel()
        );
    }

    public static Security iosDeleteAccessToken (UserRes user) {

        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                user.getAndroidJwtModel(),
                IosJWT.jwtDeleteAccessToken(user),
                user.getWebJwtModel(),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountStatusModel()
        );
    }

    public static Security iosUpdateAccessTokenDeleteOtherAccessTokens(UserRes user, NewAccTokenData tokenData) {

        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                AndroidJWT.jwtDeleteAccessToken(user),
                IosJWT.jwtUpdateAccessToken(user, tokenData),
                WebJWT.jwtDeleteAccessToken(user),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountStatusModel()
        );
    }

    public static Security iosUpdateTokensDeleteOtherTokens
            (UserRes user, NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {

        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                AndroidJWT.jwtDeleteTokens(),
                IosJWT.jwtUpdate(accessTokenData, refreshTokenData),
                WebJWT.jwtDeleteTokens(),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountStatusModel());
    }
}
