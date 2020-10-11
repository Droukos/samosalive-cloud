package com.droukos.userservice.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@ToString
@Document
@AllArgsConstructor
public class RoleModel {

  private String role;
  private boolean active;
  private LocalDateTime added;
  private String addedBy;

  public static RoleModel build(String role, String username) {
    return new RoleModel(role, false, LocalDateTime.now(), username);
  }
}
