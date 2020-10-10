package com.droukos.authservice.model.factories.user.system.logout;

import com.droukos.authservice.model.factories.user.security.login.SecurityFactoryAndroidLogout;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.UserSystem;

public class SystemFactoryAndroidLogout {
    private SystemFactoryAndroidLogout() {}

    public static UserSystem androidLogout(UserRes user) {
        return new UserSystem(
                user.getSys().getCredStars(),
                user.getSys().getAccC(),
                user.getSys().getAccC(),
                SecurityFactoryAndroidLogout.androidLogout(user)
        );
    }
}
