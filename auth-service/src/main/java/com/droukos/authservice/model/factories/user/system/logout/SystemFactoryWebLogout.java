package com.droukos.authservice.model.factories.user.system.logout;

import com.droukos.authservice.model.factories.user.security.login.SecurityFactoryWebLogout;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.UserSystem;

public class SystemFactoryWebLogout {
    private SystemFactoryWebLogout() {}

    public static UserSystem webLogout(UserRes user) {
        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                SecurityFactoryWebLogout.webLogout(user)
        );
    }

}
