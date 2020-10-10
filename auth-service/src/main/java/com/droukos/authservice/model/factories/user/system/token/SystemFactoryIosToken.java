package com.droukos.authservice.model.factories.user.system.token;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.factories.user.security.token.SecurityFactoryIosToken;
import com.droukos.authservice.model.user.system.UserSystem;

public class SystemFactoryIosToken {
    private SystemFactoryIosToken() {}

    public static UserSystem updateIosAccessToken(UserRes user, NewAccTokenData tokenData) {
        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                SecurityFactoryIosToken.iosUpdateAccessToken(user, tokenData)
        );
    }

    public static UserSystem iosUpdateOnlyTokens
            (UserRes user, NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {

        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                SecurityFactoryIosToken.iosUpdateOnlyTokens(user, accessTokenData, refreshTokenData)
        );
    }

    public static UserSystem updateIosAccessTokenDeleteOtherAccessTokens(UserRes user, NewAccTokenData tokenData) {
        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                SecurityFactoryIosToken.iosUpdateAccessTokenDeleteOtherAccessTokens(user, tokenData)
        );
    }

    public static UserSystem updateIosTokensDeleteOthers
            (UserRes user, NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {

        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                SecurityFactoryIosToken.iosUpdateTokensDeleteOtherTokens(user, accessTokenData, refreshTokenData)
        );
    }
}
