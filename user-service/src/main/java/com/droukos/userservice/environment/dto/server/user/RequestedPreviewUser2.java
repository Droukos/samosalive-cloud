package com.droukos.userservice.environment.dto.server.user;

import com.droukos.userservice.environment.constants.FieldNames;
import com.droukos.userservice.model.user.RoleModel;
import com.droukos.userservice.model.user.UserRes;
import lombok.*;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RequestedPreviewUser2 {
    private String id;
    private String user;
    private String name;
    private String sur;
    private String avatar;
    private boolean on;
    private int status;
    private List<String> roles;

    public static Mono<RequestedPreviewUser2> buildMono(Tuple2<UserRes, Set<String>> tuple2) {
        return Mono.just(build(tuple2));
    }

    public static RequestedPreviewUser2 build(Tuple2<UserRes, Set<String>> tuple2) {
        UserRes user = tuple2.getT1();
        Set<String> fieldsToNullify = tuple2.getT2();

        String avatar = (user.getProfileModel() == null) ? null : user.getAvatar();

        boolean showFullname = fieldsToNullify.contains(FieldNames.F_PRIVY_FULLNAME);
        boolean showAppStateOn = fieldsToNullify.contains(FieldNames.F_PRIVY_ONLINE_STATUS);

        return new RequestedPreviewUser2(
                user.getId(),
                user.getUserC(),
                showFullname ? null : user.getName(),
                showFullname ? null : user.getSurname(),
                avatar,
                !showAppStateOn && user.getAppState().isOnline(),
                user.getAppState().getStatus(),
                user.getAllRoles().stream().map(RoleModel::getRole).collect(Collectors.toList()));
    }
}
