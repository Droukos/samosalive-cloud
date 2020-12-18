package com.droukos.authservice.model.factories.user.security.token;

import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.security.Security;
import com.droukos.authservice.model.user.system.security.jwt.platforms.AndroidJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.IosJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.WebJWT;

public class SecurityFactoryAllToken {
    private SecurityFactoryAllToken() {}

    public static Security allDeleteAccessToken (UserRes user) {

        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                AndroidJWT.jwtDeleteAccessToken(user),
                IosJWT.jwtDeleteAccessToken(user),
                WebJWT.jwtDeleteAccessToken(user),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountStatusModel());
    }

    public static Security deleteAllTokens(UserRes user) {
        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                AndroidJWT.jwtDeleteTokens(),
                IosJWT.jwtDeleteTokens(),
                WebJWT.jwtDeleteTokens(),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountStatusModel());
    }
}
