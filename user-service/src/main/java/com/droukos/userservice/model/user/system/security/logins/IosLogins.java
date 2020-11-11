package com.droukos.userservice.model.user.system.security.logins;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class IosLogins {
  private LocalDateTime lLogin;
  private LocalDateTime lLogout;
}
