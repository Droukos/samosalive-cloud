package com.droukos.authservice.model.factories.user.system.login;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.UserSystem;
import com.droukos.authservice.model.user.system.security.Security;
import reactor.util.function.Tuple3;

public class SystemFactoryWebLogin {
    private SystemFactoryWebLogin() {}

    public static UserSystem onlyWebLogin(Tuple3<UserRes, NewAccTokenData, NewRefTokenData> tuple3) {
        UserRes user = tuple3.getT1();
        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                Security.webLogin(tuple3)
        );
    }
}
