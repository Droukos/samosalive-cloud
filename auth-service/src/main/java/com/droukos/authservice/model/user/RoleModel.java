package com.droukos.authservice.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
@Document
@AllArgsConstructor
public class RoleModel {

  private String role;
  private boolean active;
  private LocalDateTime added;
  private String addedBy;
}
