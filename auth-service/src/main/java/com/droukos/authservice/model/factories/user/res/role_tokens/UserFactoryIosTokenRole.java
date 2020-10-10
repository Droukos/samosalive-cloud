package com.droukos.authservice.model.factories.user.res.role_tokens;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.model.factories.user.system.token.SystemFactoryIosToken;
import com.droukos.authservice.model.user.RoleModel;
import com.droukos.authservice.model.user.UserRes;

public class UserFactoryIosTokenRole {
    private UserFactoryIosTokenRole() {}

    public static UserRes addRoleIosAccessTokens(UserRes user, NewAccTokenData tokenData, String newRole, String addedBy) {
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
                SystemFactoryIosToken.updateIosAccessTokenDeleteOtherAccessTokens(user, tokenData),
                user.getAppState());
    }

    public static UserRes removedRoleIosAccessTokens(UserRes user, NewAccTokenData tokenData, String oldRole) {
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
                SystemFactoryIosToken.updateIosAccessTokenDeleteOtherAccessTokens(user, tokenData),
                user.getAppState());
    }
}
