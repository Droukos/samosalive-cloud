package com.droukos.authservice.model.user.system.security.logins;

import com.droukos.authservice.model.user.UserRes;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class WebLogins {
  LocalDateTime lLogin;
  LocalDateTime lLogout;
  public static WebLogins noLoginUpdate(UserRes user) {
    return new WebLogins(
            user.getSys().getSec().getWebLog().getLLogin(),
            user.getSys().getSec().getWebLog().getLLogout()
    );
  }

  public static WebLogins lastLoginNow(UserRes user) {
    return new WebLogins(
            LocalDateTime.now(),
            user.getSys().getSec().getWebLog().getLLogout()
    );
  }

  public static WebLogins lastLogoutNow(UserRes user) {
    return new WebLogins(
            user.getSys().getSec().getWebLog().getLLogin(),
            LocalDateTime.now()
    );
  }
}
