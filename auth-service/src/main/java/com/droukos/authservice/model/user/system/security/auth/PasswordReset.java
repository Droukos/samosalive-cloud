package com.droukos.authservice.model.user.system.security.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PasswordReset {

  private String ip;
  private LocalDateTime date;

  public static PasswordReset newInfo() {
    return new PasswordReset(null, null);
  }
}
