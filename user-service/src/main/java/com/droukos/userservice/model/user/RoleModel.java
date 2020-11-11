package com.droukos.userservice.model.user;

import com.droukos.userservice.util.RolesUtil;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@ToString
@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RoleModel {

  private String role;
  private String code;
  private boolean active;
  private LocalDateTime added;
  private String addedBy;

  public static RoleModel buildRoleModelWithRoleCode(RoleModel roleModel) {
    return new RoleModel(
            roleModel.getRole(),
            RolesUtil.roleCode(roleModel.getRole()),
            roleModel.isActive(),
            roleModel.getAdded(),
            roleModel.getAddedBy());
  }
}
