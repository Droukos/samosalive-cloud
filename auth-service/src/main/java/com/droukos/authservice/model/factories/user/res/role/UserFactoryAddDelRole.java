package com.droukos.authservice.model.factories.user.res.role;

import com.droukos.authservice.model.user.RoleModel;
import com.droukos.authservice.model.user.UserRes;

public class UserFactoryAddDelRole {
    private UserFactoryAddDelRole() {}

    public static UserRes getUserWithAddedRole(UserRes user, String newRole, String addedBy) {
        return new UserRes(
                user.getId(),
                user.getUser(),
                user.getUserC(),
                user.getPass(),
                user.getPassC(),
                user.getEmail(),
                user.getEmailC(),
                RoleModel.addRole(user, newRole, addedBy),
                user.getPrsn(),
                user.getPrivy(),
                user.getSys(),
                user.getChannelSubs(),
                user.getAppState());
    }

    public static UserRes getUserWithDelRole(UserRes user, String oldRole) {
        return new UserRes(
                user.getId(),
                user.getUser(),
                user.getUserC(),
                user.getPass(),
                user.getPassC(),
                user.getEmail(),
                user.getEmailC(),
                RoleModel.removeRole(user, oldRole),
                user.getPrsn(),
                user.getPrivy(),
                user.getSys(),
                user.getChannelSubs(),
                user.getAppState());
    }
}
