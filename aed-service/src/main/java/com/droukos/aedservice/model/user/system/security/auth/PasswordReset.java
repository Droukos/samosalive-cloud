package com.droukos.aedservice.model.user.system.security.auth;

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

  String ip;
  LocalDateTime date;

  public static PasswordReset newInfo() {
    return new PasswordReset(null, null);
  }
}
