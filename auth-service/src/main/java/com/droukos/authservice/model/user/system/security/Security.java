package com.droukos.authservice.model.user.system.security;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.Verification;
import com.droukos.authservice.model.user.system.security.auth.PasswordReset;
import com.droukos.authservice.model.user.system.security.jwt.platforms.AndroidJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.IosJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.WebJWT;
import com.droukos.authservice.model.user.system.security.logins.AndroidLogins;
import com.droukos.authservice.model.user.system.security.logins.IosLogins;
import com.droukos.authservice.model.user.system.security.logins.WebLogins;
import lombok.*;
import reactor.util.function.Tuple3;

import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Security {
  private AndroidLogins androidLog;
  private IosLogins iosLog;
  private WebLogins webLog;
  private List<PasswordReset> passResets;
  private AndroidJWT androidJWT;
  private IosJWT iosJWT;
  private WebJWT webJWT;
  private Verification verified;
  private AccountLocked lock;
  private AccountStatus accStat;

  public static Security androidLogin(Tuple3<UserRes, NewAccTokenData, NewRefTokenData> tuple3) {
    UserRes user = tuple3.getT1();
    return new Security(
            AndroidLogins.lastLoginNow(user),
            user.getIosLoginsModel(),
            user.getWebLoginsModel(),
            user.getPasswordResetList(),
            AndroidJWT.jwtUpdate(tuple3.getT2(), tuple3.getT3()),
            user.getIosJwtModel(),
            user.getWebJwtModel(),
            user.getVerificationModel(),
            user.getAccountLockedModel(),
            user.getAccountStatusModel()
    );
  }

  public static Security iosLogin(Tuple3<UserRes, NewAccTokenData, NewRefTokenData> tuple3) {
    UserRes user = tuple3.getT1();
    return new Security(
            user.getAndroidLoginsModel(),
            IosLogins.lastLoginNow(user),
            user.getWebLoginsModel(),
            user.getPasswordResetList(),
            user.getAndroidJwtModel(),
            IosJWT.jwtUpdate(tuple3.getT2(), tuple3.getT3()),
            user.getWebJwtModel(),
            user.getVerificationModel(),
            user.getAccountLockedModel(),
            user.getAccountStatusModel()
    );
  }

  public static Security webLogin(Tuple3<UserRes, NewAccTokenData, NewRefTokenData> tuple3) {
    UserRes user = tuple3.getT1();
    return new Security(
            user.getAndroidLoginsModel(),
            user.getIosLoginsModel(),
            WebLogins.lastLoginNow(user),
            user.getPasswordResetList(),
            user.getAndroidJwtModel(),
            user.getIosJwtModel(),
            WebJWT.jwtUpdate(tuple3.getT2(), tuple3.getT3()),
            user.getVerificationModel(),
            user.getAccountLockedModel(),
            user.getAccountStatusModel()
    );
  }


}
