package com.droukos.authservice.environment.dto.server.auth.login;

import com.droukos.authservice.model.user.RoleModel;
import com.droukos.authservice.model.user.UserRes;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    List<String> aedEventSubs;
    List<String> aedProblemSubs;
    boolean online;
    Integer availability;

    public static LoginResponse build(UserRes user, String accessToken) {
        ArrayList<String> eventChannels = user.getChannelSubs().getAedPrSubs() != null ?
                new ArrayList<>(user.getChannelSubs().getAedEvSubs().keySet())
                : new ArrayList<>();
        ArrayList<String> problemChannels = user.getChannelSubs().getAedPrSubs() != null ?
                new ArrayList<>(user.getChannelSubs().getAedPrSubs().keySet())
                : new ArrayList<>();
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
                user.getChannelSubs() == null ? null : eventChannels,
                user.getChannelSubs() == null ? null : problemChannels,
                user.getAppState().isOn(),
                user.getAppState().getStatus());
    }
}
