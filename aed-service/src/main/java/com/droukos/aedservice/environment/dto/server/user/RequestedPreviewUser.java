package com.droukos.aedservice.environment.dto.server.user;

import com.droukos.aedservice.model.user.RoleModel;
import com.droukos.aedservice.model.user.UserRes;
import com.droukos.aedservice.util.RolesUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    public static RequestedPreviewUser build(UserRes user) {
        return new RequestedPreviewUser(
                user.getId(),
                user.getUser(),
                user.getPrsn().getProf() != null ? user.getAvatar() : null,
                user.getAppState().getStatus(),
                user.getAllRoles().stream().map(RoleModel::getRole).collect(Collectors.toList())
        );
    }
}
