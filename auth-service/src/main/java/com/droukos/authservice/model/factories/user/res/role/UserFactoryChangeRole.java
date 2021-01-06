package com.droukos.authservice.model.factories.user.res.role;

import com.droukos.authservice.model.user.RoleModel;
import com.droukos.authservice.model.user.UserRes;
import reactor.core.publisher.Mono;

public class UserFactoryChangeRole {
    private UserFactoryChangeRole() {}

    public static Mono<UserRes> changeUserRoleMono(UserRes user, String oldRole, String newRole, String addedBy) {
        return Mono.just(changeUserRole(user, oldRole, newRole, addedBy));
    }

    public static UserRes changeUserRole(UserRes user, String oldRole, String newRole, String addedBy) {
        return new UserRes(
                user.getId(),
                user.getUser(),
                user.getUserC(),
                user.getPass(),
                user.getPassC(),
                user.getEmail(),
                user.getEmailC(),
                RoleModel.changeRole(user, oldRole, newRole, addedBy),
                user.getPrsn(),
                user.getPrivy(),
                user.getSys(),
                user.getChannelSubs(),
                user.getAppState());
    }
}
