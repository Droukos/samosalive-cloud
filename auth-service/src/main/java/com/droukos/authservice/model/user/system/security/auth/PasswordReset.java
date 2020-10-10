package com.droukos.authservice.model.user.system.security.auth;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PasswordReset {

  String ip;
  LocalDateTime date;

  public static PasswordReset newInfo() {
    return new PasswordReset(null, null);
  }
}
