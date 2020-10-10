package com.droukos.authservice.model.user.system.security.logins;

import com.droukos.authservice.model.user.UserRes;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class IosLogins {
  LocalDateTime lLogin;
  LocalDateTime lLogout;

  public static IosLogins noLoginUpdate(UserRes user) {
    return new IosLogins(
            user.getSys().getSec().getIosLog().getLLogin(),
            user.getSys().getSec().getIosLog().getLLogout()
    );
  }

  public static IosLogins lastLoginNow(UserRes user) {
    return new IosLogins(
            LocalDateTime.now(),
            user.getSys().getSec().getIosLog().getLLogout()
    );
  }

  public static IosLogins lastLogoutNow(UserRes user) {
    return new IosLogins(
            user.getSys().getSec().getIosLog().getLLogin(),
            LocalDateTime.now()
    );
  }
}
