package com.droukos.authservice.environment.dto.server.auth.login;


import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.RoleModel;
import lombok.*;

import java.util.List;

@Value
public class LoginResponse {
    String accessToken;
    String userid;
    String username;
    String name;
    String surname;
    String email;
    String avatar;
    String description;
    List<RoleModel> roleModels;
    boolean online;
    Integer availability;

    public static LoginResponse build(UserRes user, String accessToken) {
        return new LoginResponse(
                accessToken,
                user.getId(),
                user.getUserC(),
                user.getName(),
                user.getSurname(),
                user.getEmailC(),
                user.getAvatar(),
                user.getDescription(),
                user.getAllRoles(),
                user.getAppState().isOn(),
                user.getAppState().getStatus());
    }
}
