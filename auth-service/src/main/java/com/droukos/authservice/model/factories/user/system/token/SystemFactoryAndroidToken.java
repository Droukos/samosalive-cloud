package com.droukos.authservice.model.factories.user.system.token;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.model.factories.user.security.token.SecurityFactoryAndroidToken;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.UserSystem;

public class SystemFactoryAndroidToken {
    private SystemFactoryAndroidToken() {}

    public static UserSystem updateAndroidAccessTokenDeleteOtherAccessTokens(UserRes user, NewAccTokenData tokenData) {
        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                SecurityFactoryAndroidToken.androidUpdateAccessTokenDeleteOtherAccessTokens(user, tokenData)
        );
    }

    public static UserSystem updateAndroidAccessToken(UserRes user, NewAccTokenData tokenData) {
        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                SecurityFactoryAndroidToken.androidUpdateAccessToken(user, tokenData)
        );
    }

    public static UserSystem updateAndroidTokensDeleteOthers
            (UserRes user, NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {

        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                SecurityFactoryAndroidToken.androidUpdateTokensDeleteOtherTokens(user, accessTokenData, refreshTokenData)
        );
    }

    public static UserSystem updateAndroidTokensOnly
            (UserRes user, NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {

        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                SecurityFactoryAndroidToken.androidUpdateTokensDeleteOtherTokens(user, accessTokenData, refreshTokenData)
        );
    }
}
