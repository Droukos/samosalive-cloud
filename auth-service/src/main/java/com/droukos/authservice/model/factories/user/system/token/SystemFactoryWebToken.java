package com.droukos.authservice.model.factories.user.system.token;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.factories.user.security.token.SecurityFactoryWebToken;
import com.droukos.authservice.model.user.system.UserSystem;

public class SystemFactoryWebToken {
    private SystemFactoryWebToken() {}

    public static UserSystem updateWebAccessToken(UserRes user, NewAccTokenData tokenData) {
        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                SecurityFactoryWebToken.webUpdateAccessToken(user, tokenData)
        );
    }

    public static UserSystem webUpdateOnlyTokens
            (UserRes user, NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {

        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                SecurityFactoryWebToken.webUpdateOnlyTokens(user, accessTokenData, refreshTokenData));
    }


        public static UserSystem updateWebAccessTokenDeleteOtherAccessTokens(UserRes user, NewAccTokenData tokenData) {
        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                SecurityFactoryWebToken.webUpdateAccessTokenDeleteOtherAccessTokens(user, tokenData)
        );
    }

    public static UserSystem updateWebTokensDeleteOthers
            (UserRes user, NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {

        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                SecurityFactoryWebToken.webUpdateTokensDeleteOtherTokens(user, accessTokenData, refreshTokenData)
        );
    }
}
