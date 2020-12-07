package com.droukos.authservice.environment.dto.server.auth.login;

import com.droukos.authservice.model.user.RoleModel;
import com.droukos.authservice.model.user.UserRes;
import lombok.Value;

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
        user.getChannelSubs().getAedEvSubs(),
        user.getChannelSubs().getAedPrSubs(),
        user.getAppState().isOn(),
        user.getAppState().getStatus());
  }
}
