package com.droukos.authservice.model.factories.user.system.token;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.factories.user.security.token.SecurityFactoryAllToken;
import com.droukos.authservice.model.user.system.UserSystem;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;

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

    public static UserSystem updateTokens(Tuple4<UserRes, NewAccTokenData, NewAccTokenData, NewAccTokenData> tuple4) {
        UserRes user = tuple4.getT1();
        return new UserSystem(
                user.getCredStars(),
                user.getAccountCreated(),
                user.getAccountUpdated(),
                SecurityFactoryAllToken.updateTokens(tuple4)
        );
    }
}
