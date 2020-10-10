package com.droukos.authservice.model.factories.user.system.logout;

import com.droukos.authservice.model.factories.user.security.login.SecurityFactoryIosLogout;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.UserSystem;

public class SystemFactoryIosLogout {
    private SystemFactoryIosLogout() {}

    public static UserSystem iosLogout(UserRes user) {
        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                SecurityFactoryIosLogout.iosLogout(user)
        );
    }
}
