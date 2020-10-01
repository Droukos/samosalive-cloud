package com.droukos.authservice.environment.dto.server.auth.login;


import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.Role;
import lombok.*;

import java.util.List;

@Data @ToString
@AllArgsConstructor @NoArgsConstructor
@Setter @Getter
public class LoginResponse {
    private String accessToken;
    private String userid;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String avatar;
    private String description;
    private List<Role> roles;
    private boolean online;
    private Integer availability;

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
