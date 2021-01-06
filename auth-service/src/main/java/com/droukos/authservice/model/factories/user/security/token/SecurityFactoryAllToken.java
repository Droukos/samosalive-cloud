package com.droukos.authservice.model.factories.user.security.token;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.security.Security;
import com.droukos.authservice.model.user.system.security.jwt.platforms.AndroidJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.IosJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.WebJWT;
import reactor.util.function.Tuple4;

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

    public static Security updateTokens(Tuple4<UserRes, NewAccTokenData, NewAccTokenData, NewAccTokenData> tuple4) {
        UserRes user = tuple4.getT1();
        NewAccTokenData androidAccTokenData = tuple4.getT2();
        NewAccTokenData iosAccTokenData = tuple4.getT3();
        NewAccTokenData webAccTokenData =tuple4.getT4();

        return new Security(
                user.getAndroidLoginsModel(),
                user.getIosLoginsModel(),
                user.getWebLoginsModel(),
                user.getPasswordResetList(),
                androidAccTokenData.getUserId() != null ? AndroidJWT.jwtAccessTokenUpdate(user, androidAccTokenData) : null,
                iosAccTokenData.getUserId() != null ? IosJWT.jwtAccessTokenUpdate(user, iosAccTokenData) : null,
                webAccTokenData.getUserId() != null ? WebJWT.jwtAccessTokenUpdate(user, webAccTokenData) : null,
                user.getVerificationModel(),
                user.getAccountLockedModel(),
                user.getAccountStatusModel());
    }
}
