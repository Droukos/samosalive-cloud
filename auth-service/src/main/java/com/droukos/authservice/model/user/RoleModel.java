package com.droukos.authservice.model.user;

import com.droukos.authservice.util.RolesUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleModel {

  private String role;
  @Transient private String code;
  private boolean active;
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
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

  public static List<RoleModel> changeRole(UserRes user, String oldRole, String newRole, String addedBy) {

    return Stream.concat(
            user.getAllRoles().stream().filter(roleModel -> !roleModel.getRole().equals(oldRole)),
            Stream.of(new RoleModel(newRole, null, false, LocalDateTime.now(), addedBy)))
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
