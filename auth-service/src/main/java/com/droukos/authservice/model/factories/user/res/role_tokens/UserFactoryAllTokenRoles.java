package com.droukos.authservice.model.factories.user.res.role_tokens;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.model.factories.user.system.token.SystemFactoryAllToken;
import com.droukos.authservice.model.user.RoleModel;
import com.droukos.authservice.model.user.UserRes;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;

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
                user.getChannelSubs(),
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
                user.getChannelSubs(),
                user.getAppState());
    }

    public static Mono<UserRes> updateUserAccTokensMono(Tuple4<UserRes, NewAccTokenData, NewAccTokenData, NewAccTokenData> tuple4) {
        return Mono.just(updateUserAccTokens(tuple4));
    }

    public static UserRes updateUserAccTokens(Tuple4<UserRes, NewAccTokenData, NewAccTokenData, NewAccTokenData> tuple4) {
        UserRes user = tuple4.getT1();

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
                SystemFactoryAllToken.updateTokens(tuple4),
                user.getChannelSubs(),
                user.getAppState());
    }
}
