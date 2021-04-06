package com.droukos.authservice.model.user.system.security.logins;

import com.droukos.authservice.model.user.UserRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class WebLogins {
  private LocalDateTime lLogin;
  private LocalDateTime lLogout;

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
