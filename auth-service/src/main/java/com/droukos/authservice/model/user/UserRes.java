package com.droukos.authservice.model.user;

import com.droukos.authservice.model.user.personal.Personal;
import com.droukos.authservice.model.user.privacy.PrivacySettingMap;
import com.droukos.authservice.model.user.system.UserSystem;
import com.droukos.authservice.model.user.system.Verification;
import com.droukos.authservice.model.user.system.security.AccountLocked;
import com.droukos.authservice.model.user.system.security.AccountStatus;
import com.droukos.authservice.model.user.system.security.auth.PasswordReset;
import com.droukos.authservice.model.user.system.security.jwt.platforms.AndroidJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.IosJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.WebJWT;
import com.droukos.authservice.model.user.system.security.logins.AndroidLogins;
import com.droukos.authservice.model.user.system.security.logins.IosLogins;
import com.droukos.authservice.model.user.system.security.logins.WebLogins;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document
public class UserRes {

  private @Id String id;
  private @Indexed String user;
  private String userC;
  private String pass;
  private @Transient String passC;
  private @Indexed String email;
  private String emailC;
  private @Indexed List<RoleModel> allRoles;
  private Personal prsn;
  private PrivacySettingMap privy;
  private UserSystem sys;
  private ChannelSubs channelSubs;
  private AppState appState;

  /*
  * User entity factories
  */

  public static UserRes passwordUpdate(String newPass, UserRes user) {

    return new UserRes(
        user.id,
        user.user,
        user.userC,
        newPass,
        user.passC,
        user.email,
        user.emailC,
        user.allRoles,
        user.prsn,
        user.privy,
        user.sys,
        user.getChannelSubs(),
        user.appState);
  }

  /*
   * Shortcut getters for inside user objects
   */

  public String getName() {
    return this.prsn.getName();
  }

  public String getSurname() {
    return this.prsn.getSur();
  }

  public String getDescription() {
    return this.prsn.getProf().getDesc();
  }

  public String getAvatar() {
    return this.prsn.getProf().getAv();
  }

  public AndroidJWT getAndroidJwtModel() {
    return this.sys.getSec().getAndroidJWT();
  }

  public IosJWT getIosJwtModel() {
    return this.getSys().getSec().getIosJWT();
  }

  public WebJWT getWebJwtModel() {
    return this.getSys().getSec().getWebJWT();
  }

  public AndroidLogins getAndroidLoginsModel() { return this.getSys().getSec().getAndroidLog();}

  public IosLogins getIosLoginsModel() { return this.getSys().getSec().getIosLog(); }

  public WebLogins getWebLoginsModel() { return this.getSys().getSec().getWebLog(); }

  public List<PasswordReset> getPasswordResetList() { return this.getSys().getSec().getPassResets(); }

  public Verification getVerificationModel() { return this.getSys().getSec().getVerified(); }

  public Double getCredStars() { return this.getSys().getCredStars(); }

  public LocalDateTime getAccountCreated() { return this.getSys().getAccC(); }

  public LocalDateTime getAccountUpdated() { return this.getSys().getAccU(); }

  public AccountLocked getAccountLockedModel() { return this.getSys().getSec().getLock(); }

  public AccountStatus getAccountStatusModel() { return this.getSys().getSec().getAccStat(); }

  public LocalDateTime getAndroidLastLogin() {
    return this.getSys().getSec().getAndroidLog().getLLogin();
  }

  public LocalDateTime getAndroidLastLogout() {
    return this.getSys().getSec().getAndroidLog().getLLogout();
  }

  public String getAndroidAccessTokenId() {
    return this.getSys().getSec().getAndroidJWT().getAccToken().getAid();
  }

  public String getIosAccessTokenId() {
    return this.getSys().getSec().getIosJWT().getAccToken().getAid();
  }

  public String getWebAccessTokenId() {
    return this.getSys().getSec().getWebJWT().getAccToken().getAid();
  }
}
