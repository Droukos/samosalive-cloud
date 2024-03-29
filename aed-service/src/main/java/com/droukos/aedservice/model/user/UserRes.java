package com.droukos.aedservice.model.user;

import com.droukos.aedservice.model.user.personal.Personal;
import com.droukos.aedservice.model.user.privacy.PrivacySettingMap;
import com.droukos.aedservice.model.user.system.UserSystem;
import com.droukos.aedservice.model.user.system.Verification;
import com.droukos.aedservice.model.user.system.security.AccountBanned;
import com.droukos.aedservice.model.user.system.security.AccountLocked;
import com.droukos.aedservice.model.user.system.security.auth.PasswordReset;
import com.droukos.aedservice.model.user.system.security.jwt.platforms.AndroidJWT;
import com.droukos.aedservice.model.user.system.security.jwt.platforms.IosJWT;
import com.droukos.aedservice.model.user.system.security.jwt.platforms.WebJWT;
import com.droukos.aedservice.model.user.system.security.logins.AndroidLogins;
import com.droukos.aedservice.model.user.system.security.logins.IosLogins;
import com.droukos.aedservice.model.user.system.security.logins.WebLogins;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
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

  public AccountLocked getAccountLockedModel() { return this.getSys().getSec().getLock(); }

  public AccountBanned getAccountBannedModel() { return this.getSys().getSec().getBan(); }

  public LocalDateTime getAndroidLastLogin() {
    return this.getSys().getSec().getAndroidLog().getLLogin();
  }

  public LocalDateTime getAndroidLastLogout() {
    return this.getSys().getSec().getAndroidLog().getLLogout();
  }

  public String getAndroidAccessTokenId() {
    return this.getSys().getSec().getAndroidJWT().getAccToken().getAid();
  }
}
