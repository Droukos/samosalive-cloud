package com.droukos.userservice.model.factories.user.res;

import com.droukos.userservice.environment.dto.client.user.UpdateAvailability;
import com.droukos.userservice.model.user.AppState;
import com.droukos.userservice.model.user.UserRes;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public class UserFactoryAvailability {
    private UserFactoryAvailability() {}

    public static Mono<UserRes> buildMonoUserOnAvailbilityUpdate(Tuple2<UpdateAvailability, UserRes> tuple2) {
        return Mono.just(buildUserOnAvailabilityUpdate(tuple2));
    }

    public static UserRes buildUserOnAvailabilityUpdate(Tuple2<UpdateAvailability, UserRes> tuple2) {
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
                user.getPrivy(),
                user.getSys(),
                new AppState(user.getAppState().isOn(), tuple2.getT1().getStatus())
        );
    }
}
