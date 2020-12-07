package com.droukos.authservice.model.factories.user.res.tokens;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.factories.user.system.token.SystemFactoryAndroidToken;

public class UserFactoryAndroidTokens {
    private UserFactoryAndroidTokens() {}

    public static UserRes updateAndroidAccessTokenOnly(UserRes user, NewAccTokenData accessTokenData) {
        return new UserRes(
                user.getId(),
                user.getUser(),
                user.getUserC(),
                user.getPass(),
                user.getPassC(),
                user.getEmail(),
                user.getEmailC(),
                user.getAllRoles(),
                user.getPrsn(),
                user.getPrivy(),
                SystemFactoryAndroidToken.updateAndroidAccessToken(user, accessTokenData),
                user.getChannelSubs(),
                user.getAppState()
        );
    }

    public static UserRes updateAndroidTokensOnly
            (UserRes user, NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {

        return new UserRes(
                user.getId(),
                user.getUser(),
                user.getUserC(),
                user.getPass(),
                user.getPassC(),
                user.getEmail(),
                user.getEmailC(),
                user.getAllRoles(),
                user.getPrsn(),
                user.getPrivy(),
                SystemFactoryAndroidToken.updateAndroidTokensOnly(user, accessTokenData, refreshTokenData),
                user.getChannelSubs(),
                user.getAppState()
        );
    }

    public static UserRes updateAndroidTokensDeleteOthers
            (UserRes user, NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {

        return new UserRes(
                user.getId(),
                user.getUser(),
                user.getUserC(),
                user.getPass(),
                user.getPassC(),
                user.getEmail(),
                user.getEmailC(),
                user.getAllRoles(),
                user.getPrsn(),
                user.getPrivy(),
                SystemFactoryAndroidToken.updateAndroidTokensDeleteOthers(user, accessTokenData, refreshTokenData),
                user.getChannelSubs(),
                user.getAppState());
    }
}
