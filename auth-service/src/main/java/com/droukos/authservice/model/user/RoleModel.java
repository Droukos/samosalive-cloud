package com.droukos.authservice.model.user;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ToString
@Getter
@Document
@AllArgsConstructor
public class RoleModel {

  private final String role;
  private final boolean active;
  private final LocalDateTime added;
  private final String addedBy;

  public static List<RoleModel> addRole(UserRes user, String newRole, String addedBy) {
    return Stream.concat(user.getAllRoles().stream(),
            Stream.of(new RoleModel(newRole, false, LocalDateTime.now(), addedBy)))
            .collect(Collectors.toList());
  }

  public static List<RoleModel> removeRole(UserRes user, String removedRole) {
    return user.getAllRoles()
            .stream()
            .filter(roleModel -> !roleModel.getRole().equals(removedRole))
            .collect(Collectors.toList());
  }
}
