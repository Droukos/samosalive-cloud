package com.droukos.aedservice.model.user.system.security.logins;

import com.droukos.aedservice.model.user.UserRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class AndroidLogins {
  LocalDateTime lLogin;
  LocalDateTime lLogout;

  public static AndroidLogins noLoginUpdate (UserRes user) {
    return new AndroidLogins(
            user.getSys().getSec().getAndroidLog().lLogin,
            user.getSys().getSec().getAndroidLog().lLogout
    );
  }
  public static AndroidLogins lastLoginNow(UserRes user) {
    return new AndroidLogins(
            LocalDateTime.now(),
            user.getAndroidLastLogout()
    );
  }

  public static AndroidLogins lastLogoutNow(UserRes user) {
    return new AndroidLogins(
            user.getAndroidLastLogin(),
            LocalDateTime.now()
    );
  }
}
