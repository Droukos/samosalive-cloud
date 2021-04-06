package com.droukos.authservice.environment.dto.client.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UpdateRole {
  private String updatedRole;
  private List<String> rolesOnDb;
}
