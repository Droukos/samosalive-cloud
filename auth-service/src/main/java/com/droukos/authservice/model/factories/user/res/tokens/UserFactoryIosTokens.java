package com.droukos.authservice.model.factories.user.res.tokens;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.model.factories.user.system.token.SystemFactoryIosToken;
import com.droukos.authservice.model.user.UserRes;

public class UserFactoryIosTokens {
    private UserFactoryIosTokens() {}

    public static UserRes updateIosTokensOnly
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
                SystemFactoryIosToken.iosUpdateOnlyTokens(user, accessTokenData, refreshTokenData),
                user.getAppState()
        );
    }

    public static UserRes updateIosAccessTokenOnly(UserRes user, NewAccTokenData accessTokenData) {
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
                SystemFactoryIosToken.updateIosAccessToken(user, accessTokenData),
                user.getAppState()
        );
    }

    public static UserRes updateIosTokensDeleteOthers
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
                SystemFactoryIosToken.updateIosTokensDeleteOthers(user, accessTokenData, refreshTokenData),
                user.getAppState());
    }
}
