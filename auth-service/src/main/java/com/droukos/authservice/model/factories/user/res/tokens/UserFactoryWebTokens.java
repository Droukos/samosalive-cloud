package com.droukos.authservice.model.factories.user.res.tokens;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.model.factories.user.system.token.SystemFactoryWebToken;
import com.droukos.authservice.model.user.UserRes;

public class UserFactoryWebTokens {
    private UserFactoryWebTokens() {}
    public static UserRes updateWebTokensOnly
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
                SystemFactoryWebToken.webUpdateOnlyTokens(user, accessTokenData, refreshTokenData),
                user.getAppState()
        );
    }

    public static UserRes updateWebAccessTokenOnly(UserRes user, NewAccTokenData accessTokenData) {
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
                SystemFactoryWebToken.updateWebAccessToken(user, accessTokenData),
                user.getAppState()
        );
    }
    public static UserRes updateWebTokensDeleteOthers
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
                SystemFactoryWebToken.updateWebTokensDeleteOthers(user, accessTokenData, refreshTokenData),
                user.getAppState());
    }
}
