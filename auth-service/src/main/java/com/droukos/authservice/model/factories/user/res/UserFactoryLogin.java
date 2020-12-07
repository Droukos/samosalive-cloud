package com.droukos.authservice.model.factories.user.res;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.model.factories.user.system.login.SystemFactoryAndroidLogin;
import com.droukos.authservice.model.factories.user.system.login.SystemFactoryIosLogin;
import com.droukos.authservice.model.factories.user.system.login.SystemFactoryWebLogin;
import com.droukos.authservice.model.user.AppState;
import com.droukos.authservice.model.user.UserRes;
import reactor.util.function.Tuple3;

public class UserFactoryLogin {
    private UserFactoryLogin() {}

    public static UserRes androidLoginUpdate(Tuple3<UserRes, NewAccTokenData, NewRefTokenData> tuple3) {

        UserRes user = tuple3.getT1();

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
                SystemFactoryAndroidLogin.androidLogin(tuple3),
                user.getChannelSubs(),
                AppState.isOnline(user));
    }

    public static UserRes iosLoginUpdate(Tuple3<UserRes, NewAccTokenData, NewRefTokenData> tuple3) {

        UserRes user = tuple3.getT1();

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
                SystemFactoryIosLogin.onlyIosLogin(tuple3),
                user.getChannelSubs(),
                AppState.isOnline(user));
    }

    public static UserRes webLoginUpdate(Tuple3<UserRes, NewAccTokenData, NewRefTokenData> tuple3) {

        UserRes user = tuple3.getT1();

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
                SystemFactoryWebLogin.onlyWebLogin(tuple3),
                user.getChannelSubs(),
                AppState.isOnline(user));
    }
}
