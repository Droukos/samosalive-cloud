package com.droukos.authservice.environment.dto.client.auth;

import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@With
@Getter
@Builder
public class UpdateEmail {
  private String pass;
  private String passOnDB;
  private String email;
  private String newEmail;
}
