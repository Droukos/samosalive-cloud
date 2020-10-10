package com.droukos.authservice.model.factories.user.system.token;

import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.factories.user.security.token.SecurityFactoryAllToken;
import com.droukos.authservice.model.user.system.UserSystem;

public class SystemFactoryAllToken {
    private SystemFactoryAllToken() {}

    public static UserSystem deleteAllAccessTokens(UserRes user) {
        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                SecurityFactoryAllToken.allDeleteAccessToken(user)
        );
    }

    public static UserSystem deleteAllTokens(UserRes user) {
        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                SecurityFactoryAllToken.deleteAllTokens(user)
        );
    }
}
