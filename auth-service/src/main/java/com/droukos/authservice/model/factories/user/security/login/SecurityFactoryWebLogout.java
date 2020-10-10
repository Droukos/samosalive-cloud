package com.droukos.authservice.model.factories.user.security.login;

import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.security.Security;
import com.droukos.authservice.model.user.system.security.jwt.platforms.WebJWT;
import com.droukos.authservice.model.user.system.security.logins.WebLogins;

public class SecurityFactoryWebLogout {
    private SecurityFactoryWebLogout() {}

    public static Security webLogout(UserRes user) {
        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                WebLogins.lastLogoutNow(user),
                user.getPasswordResetList(),
                user.getAndroidJwtModel(),
                user.getIosJwtModel(),
                WebJWT.jwtDeleteTokens(),
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountBannedModel()
        );
    }
}
