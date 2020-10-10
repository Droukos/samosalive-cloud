package com.droukos.authservice.model.factories.user.res;

import com.droukos.authservice.model.factories.user.system.logout.SystemFactoryAndroidLogout;
import com.droukos.authservice.model.factories.user.system.logout.SystemFactoryIosLogout;
import com.droukos.authservice.model.factories.user.system.logout.SystemFactoryWebLogout;
import com.droukos.authservice.model.user.AppState;
import com.droukos.authservice.model.user.UserRes;

import static com.droukos.authservice.environment.constants.Platforms.*;

public class UserFactoryLogout {
    private UserFactoryLogout() {}

    public static UserRes androidLogout(UserRes user) {
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
                SystemFactoryAndroidLogout.androidLogout(user),
                AppState.caseOtherJwtModelsNullAppStateOffline(user, ANDROID)
        );
    }

    public static UserRes iosLogout(UserRes user) {
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
                SystemFactoryIosLogout.iosLogout(user),
                AppState.caseOtherJwtModelsNullAppStateOffline(user, IOS)
        );
    }

    public static UserRes webLogout(UserRes user) {
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
                SystemFactoryWebLogout.webLogout(user),
                AppState.caseOtherJwtModelsNullAppStateOffline(user, WEB)
        );
    }
}
