package com.droukos.userservice.model.factories.user.res;

import com.droukos.userservice.environment.dto.client.user.UpdateUserPrivacy;
import com.droukos.userservice.model.user.UserRes;
import com.droukos.userservice.model.user.privacy.PrivacySettingMap;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public class UserFactoryPrivacy {
    private UserFactoryPrivacy() {}

    public static Mono<UserRes> buildMonoUpdatePrivacy(Tuple2<UpdateUserPrivacy, UserRes> tuple2) {
        return Mono.just(buildUpdatePrivacy(tuple2));
    }
    public static UserRes buildUpdatePrivacy(Tuple2<UpdateUserPrivacy, UserRes> tuple2) {
        UserRes user = tuple2.getT2();
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
                PrivacySettingMap.buildUpdatePrivacy(tuple2.getT1()),
                user.getSys(),
                user.getChannelSubs(),
                user.getAppState()
        );
    }
}
