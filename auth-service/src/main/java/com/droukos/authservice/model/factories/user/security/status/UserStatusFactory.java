package com.droukos.authservice.model.factories.user.security.status;

import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.UserSystem;
import com.droukos.authservice.model.user.system.security.AccountStatus;
import com.droukos.authservice.model.user.system.security.Security;
import reactor.core.publisher.Mono;

import static com.droukos.authservice.environment.enums.AccountStatus.ACTIVE;

public class UserStatusFactory {
    private UserStatusFactory() {}

    public static Mono<UserRes> createAccountStatusMono(UserRes user) {
        return Mono.just(createAccountStatus(user));
    }

    public static UserRes createAccountStatus(UserRes user) {
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
                new UserSystem(
                        user.getCredStars(),
                        user.getAccountCreated(),
                        user.getAccountUpdated(),
                        new Security(
                                user.getAndroidLoginsModel(),
                                user.getIosLoginsModel(),
                                user.getWebLoginsModel(),
                                user.getPasswordResetList(),
                                user.getAndroidJwtModel(),
                                user.getIosJwtModel(),
                                user.getWebJwtModel(),
                                user.getVerificationModel(),
                                user.getAccountLockedModel(),
                                new AccountStatus(ACTIVE.getCode(), null)
                        )
                ),
                user.getChannelSubs(),
                user.getAppState()
        );
    }
}
