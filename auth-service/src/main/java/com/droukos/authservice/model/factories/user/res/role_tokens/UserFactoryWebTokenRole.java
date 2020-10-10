package com.droukos.authservice.model.factories.user.res.role_tokens;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.model.factories.user.system.token.SystemFactoryWebToken;
import com.droukos.authservice.model.user.RoleModel;
import com.droukos.authservice.model.user.UserRes;

public class UserFactoryWebTokenRole {
    private UserFactoryWebTokenRole() {}

    public static UserRes addRoleWebAccessTokens(UserRes user, NewAccTokenData tokenData, String newRole, String addedBy) {
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
                SystemFactoryWebToken.updateWebAccessTokenDeleteOtherAccessTokens(user, tokenData),
                user.getAppState());
    }

    public static UserRes removedRoleWebAccessTokens(UserRes user, NewAccTokenData tokenData, String oldRole) {
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
                SystemFactoryWebToken.updateWebAccessTokenDeleteOtherAccessTokens(user, tokenData),
                user.getAppState());
    }
}
