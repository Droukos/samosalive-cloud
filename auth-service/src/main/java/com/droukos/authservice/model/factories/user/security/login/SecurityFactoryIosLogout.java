package com.droukos.authservice.model.factories.user.security.login;

import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.security.Security;
import com.droukos.authservice.model.user.system.security.jwt.platforms.IosJWT;
import com.droukos.authservice.model.user.system.security.logins.IosLogins;

public class SecurityFactoryIosLogout {
    private SecurityFactoryIosLogout() {}

    public static Security iosLogout(UserRes user) {
        return new Security(
                user.getAndroidLoginsModel(),
                IosLogins.lastLogoutNow(user),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                user.getAndroidJwtModel(),
                IosJWT.jwtDeleteTokens(),
                user.getWebJwtModel(),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountBannedModel()
        );
    }
}
