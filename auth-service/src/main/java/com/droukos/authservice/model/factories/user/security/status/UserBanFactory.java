package com.droukos.authservice.model.factories.user.security.status;

import com.droukos.authservice.environment.dto.client.admin.BanUser;
import com.droukos.authservice.environment.enums.Availability;
import com.droukos.authservice.model.user.AppState;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.UserSystem;
import com.droukos.authservice.model.user.system.security.AccountStatus;
import com.droukos.authservice.model.user.system.security.Security;
import com.droukos.authservice.model.user.system.security.logins.AndroidLogins;
import com.droukos.authservice.model.user.system.security.logins.IosLogins;
import com.droukos.authservice.model.user.system.security.logins.WebLogins;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;

public class UserBanFactory {
    private UserBanFactory() {
    }

    public static Mono<UserRes> permBanUserMono(UserRes user) {
        return Mono.just(permBanUser(user));
    }

    public static Mono<UserRes> tempBanUserMono(Tuple2<UserRes, BanUser> tuple2) {
        return Mono.just(tempBanUser(tuple2.getT1(), tuple2.getT2().getDuration()));
    }

    public static Mono<UserRes> tempBanUserMono(UserRes user, long duration) {
        return Mono.just(tempBanUser(user, duration));
    }

    public static Mono<UserRes> unbanUserMono(UserRes user) {
        return Mono.just(unbanUser(user));
    }

    public static UserRes permBanUser(UserRes user) {
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
                        LocalDateTime.now(),
                        new Security(
                                AndroidLogins.lastLoginNow(user),
                                IosLogins.lastLoginNow(user),
                                WebLogins.lastLoginNow(user),
                                user.getPasswordResetList(),
                                user.getAndroidJwtModel(),
                                user.getIosJwtModel(),
                                user.getWebJwtModel(),
                                user.getVerificationModel(),
                                user.getAccountLockedModel(),
                                AccountStatus.permBanUser()
                        )
                ),
                user.getChannelSubs(),
               new AppState(
                       false,
                       Availability.PERM_BANNED.getCode()
               )
        );
    }

    public static UserRes tempBanUser(UserRes user, long untilHours) {
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
                        LocalDateTime.now(),
                        new Security(
                                AndroidLogins.lastLoginNow(user),
                                IosLogins.lastLoginNow(user),
                                WebLogins.lastLoginNow(user),
                                user.getPasswordResetList(),
                                user.getAndroidJwtModel(),
                                user.getIosJwtModel(),
                                user.getWebJwtModel(),
                                user.getVerificationModel(),
                                user.getAccountLockedModel(),
                                AccountStatus.tempBanUser(untilHours)
                        )
                ),
                user.getChannelSubs(),
                new AppState(
                        false,
                        Availability.TEMP_BANNED.getCode()
                )
        );
    }

    public static UserRes unbanUser(UserRes user) {
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
                        LocalDateTime.now(),
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
                                AccountStatus.unbanUser()
                        )
                ),
                user.getChannelSubs(),
                new AppState(
                        false,
                        Availability.INVISIBLE.getCode()
                )
        );
    }
}
