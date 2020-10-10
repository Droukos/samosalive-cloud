package com.droukos.authservice.model.factories.user.res.role_tokens;

import com.droukos.authservice.model.user.RoleModel;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.factories.user.system.token.SystemFactoryAllToken;

public class UserFactoryAllTokenRoles {
    private UserFactoryAllTokenRoles() {}

    public static UserRes addRoleDeleteAllAccessTokens(UserRes user, String newRole, String addedBy) {
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
                SystemFactoryAllToken.deleteAllAccessTokens(user),
                user.getAppState());
    }


    public static UserRes removedRoleDeleteAllAccessTokens(UserRes user, String oldRole) {
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
                SystemFactoryAllToken.deleteAllAccessTokens(user),
                user.getAppState());
    }
}
