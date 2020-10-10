package com.droukos.authservice.model.factories.user.security.login;

import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.security.Security;
import com.droukos.authservice.model.user.system.security.jwt.platforms.AndroidJWT;
import com.droukos.authservice.model.user.system.security.logins.AndroidLogins;

public class SecurityFactoryAndroidLogout {
    private SecurityFactoryAndroidLogout() {}


    public static Security androidLogout(UserRes user) {
        return new Security(
               AndroidLogins.lastLogoutNow(user),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                AndroidJWT.jwtDeleteTokens(),
                user.getIosJwtModel(),
                user.getWebJwtModel(),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountBannedModel()
        );
    }
}
