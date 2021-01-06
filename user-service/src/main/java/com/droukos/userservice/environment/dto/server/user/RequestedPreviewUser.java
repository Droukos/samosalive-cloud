package com.droukos.userservice.environment.dto.server.user;

import com.droukos.userservice.model.user.RoleModel;
import com.droukos.userservice.model.user.UserRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestedPreviewUser {
    private String id;
    private String username;
    private String avatar;
    private int status;
    private List<String> roles;

    public static Mono<RequestedPreviewUser> buildMono(UserRes user) {
        return Mono.just(build(user));
    }

    public static RequestedPreviewUser build(UserRes user) {
        return new RequestedPreviewUser(
                user.getId(),
                user.getUser(),
                user.getAvatar(),
                user.getAppState().getStatus(),
                user.getAllRoles().stream().map(RoleModel::getRole).collect(Collectors.toList())
        );
    }

}
