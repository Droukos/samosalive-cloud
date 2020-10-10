package com.droukos.authservice.environment.dto.client.auth;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UpdateRole {
  private String updatedRole;
  private List<String> rolesOnDb;
}
