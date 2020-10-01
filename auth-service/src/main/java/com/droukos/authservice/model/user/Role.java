package com.droukos.authservice.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@ToString
@Document
@AllArgsConstructor
public class Role {

  private String code;
  private boolean active;
  private LocalDateTime added;
  private String addedBy;

  public static Role build(String role, String username) {
    return new Role(role, false, LocalDateTime.now(), username);
  }
}
