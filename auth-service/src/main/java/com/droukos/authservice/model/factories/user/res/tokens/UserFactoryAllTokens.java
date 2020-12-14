package com.droukos.authservice.model.factories.user.res.tokens;

import com.droukos.authservice.model.factories.user.system.token.SystemFactoryAllToken;
import com.droukos.authservice.model.user.UserRes;

public class UserFactoryAllTokens {
    private UserFactoryAllTokens() {}

    public static UserRes deleteAllTokens(UserRes user) {
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
                SystemFactoryAllToken.deleteAllTokens(user),
                user.getChannelSubs(),
                user.getAppState());
    }
}
