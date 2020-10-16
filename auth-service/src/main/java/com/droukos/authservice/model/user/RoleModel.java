package com.droukos.authservice.model.user;

import com.droukos.authservice.util.RolesUtil;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ToString
@Getter
@Document
@AllArgsConstructor
@NoArgsConstructor
public class RoleModel {

  private String role;
  @Transient private String code;
  private boolean active;
  private LocalDateTime added;
  private String addedBy;

  public static List<RoleModel> addRole(UserRes user, String newRole, String addedBy) {
    return Stream.concat(user.getAllRoles().stream(),
            Stream.of(new RoleModel(newRole, null, false, LocalDateTime.now(), addedBy)))
            .collect(Collectors.toList());
  }

  public static List<RoleModel> removeRole(UserRes user, String removedRole) {
    return user.getAllRoles()
            .stream()
            .filter(roleModel -> !roleModel.getRole().equals(removedRole))
            .collect(Collectors.toList());
  }

  public static RoleModel buildRoleModelWithRoleCode(RoleModel roleModel) {
    return new RoleModel(
            roleModel.getRole(),
            RolesUtil.roleCode(roleModel.getRole()),
            roleModel.isActive(),
            roleModel.getAdded(),
            roleModel.getAddedBy());
  }
}
