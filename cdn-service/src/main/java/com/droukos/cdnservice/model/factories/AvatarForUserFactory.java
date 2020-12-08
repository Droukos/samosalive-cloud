package com.droukos.cdnservice.model.factories;

import com.droukos.cdnservice.model.user.UserRes;
import com.droukos.cdnservice.model.user.personal.Personal;
import com.droukos.cdnservice.model.user.personal.Profile;
import lombok.Getter;
import lombok.ToString;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@ToString
@Getter
public class AvatarForUserFactory {
    private AvatarForUserFactory() {
    }

    public static Mono<UserRes> updateUserAvatarUrlMono(Tuple2<UserRes, String> tuple2) {
        return Mono.just(updateUserAvatarUrl(tuple2));
    }

    public static UserRes updateUserAvatarUrl(Tuple2<UserRes, String> tuple2) {
        UserRes user = tuple2.getT1();
        String url = tuple2.getT2();
        return new UserRes(
                user.getId(),
                user.getUser(),
                user.getUserC(),
                user.getPass(),
                user.getPassC(),
                user.getEmail(),
                user.getEmailC(),
                user.getAllRoles(),
                new Personal(
                        user.getPrsn().getName(),
                        user.getPrsn().getSur(),
                        new Profile(
                                url,
                                user.getProfileModel().getBk(),
                                user.getProfileModel().getDesc()
                        ),
                        user.getPrsn().getAddr(),
                        user.getPrsn().getPhones()
                ),
                user.getPrivy(),
                user.getSys(),
                user.getAppState()
        );
    }
}
