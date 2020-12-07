package com.droukos.userservice.model.factories.user.res;

import com.droukos.userservice.environment.dto.client.user.UpdateUserPersonal;
import com.droukos.userservice.model.user.UserRes;
import com.droukos.userservice.model.user.personal.Personal;

public class UserFactoryPersonal {
    private UserFactoryPersonal() {}

    public static UserRes updatePersonalInfo(UserRes user, UpdateUserPersonal updateUserPersonal) {
        return new UserRes(
                user.getId(),
                user.getUser(),
                user.getUserC(),
                user.getPass(),
                user.getPassC(),
                user.getEmail(),
                user.getEmailC(),
                user.getAllRoles(),
                Personal.updatePersonalInfo(user, updateUserPersonal),
                user.getPrivy(),
                user.getSys(),
                user.getChannelSubs(),
                user.getAppState()
        );
    }
}
