package com.droukos.authservice.environment.dto.server.auth.login;

import com.droukos.authservice.model.user.RoleModel;
import com.droukos.authservice.model.user.UserRes;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginResponse {
    private String accessToken;
    private String userid;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String avatar;
    private String description;
    private List<RoleModel> roleModels;
    private List<String> aedEventSubs;
    private List<String> aedProblemSubs;
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
                user.getAllRoles().stream()
                        .map(RoleModel::buildRoleModelWithRoleCode)
                        .collect(Collectors.toList()),
                user.getChannelSubs() == null ? null : user.getChannelSubs().getAedEvSubs() != null
                        ? new ArrayList<>(user.getChannelSubs().getAedEvSubs().keySet())
                        : new ArrayList<>(),
                user.getChannelSubs() == null ? null : user.getChannelSubs().getAedPrSubs() != null
                        ? new ArrayList<>(user.getChannelSubs().getAedPrSubs().keySet())
                        : new ArrayList<>(),
                user.getAppState().isOn(),
                user.getAppState().getStatus());
    }
}
