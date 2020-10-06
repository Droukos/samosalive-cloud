package com.droukos.authservice.model.user;

import com.droukos.authservice.environment.dto.RequesterAccessTokenData;
import com.droukos.authservice.environment.dto.RequesterRefreshTokenData;
import com.droukos.authservice.environment.dto.client.auth.UpdateEmail;
import com.droukos.authservice.environment.dto.client.auth.UpdatePassword;
import com.droukos.authservice.environment.dto.client.auth.UpdateRole;
import com.droukos.authservice.environment.dto.client.auth.login.LoginRequest;
import com.droukos.authservice.model.user.personal.Personal;
import com.droukos.authservice.model.user.privacy.PrivacySettingMap;
import com.droukos.authservice.model.user.system.UserSystem;
import com.droukos.authservice.model.user.system.security.jwt.platforms.AndroidJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.IosJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.WebJWT;
import com.droukos.authservice.model.user.system.security.jwt.tokens.AccessToken;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Document
public class UserRes {

  @Id private String id;
  @NonNull @Indexed private String user;
  @NonNull private String userC;
  @NonNull private String pass;
  @NonNull @Transient private String passC;
  @NonNull @Indexed private String email;
  @NonNull private String emailC;
  @NonNull @Indexed private List<RoleModel> allRoles;
  @NonNull private Personal prsn;
  @NonNull private PrivacySettingMap privy;
  @NonNull private UserSystem sys;
  @NonNull private AppState appState;

  /*Services Fields*/
  @Transient private ServerRequest serverRequest;
  @Transient private RequesterAccessTokenData requesterAccessTokenData;
  @Transient private RequesterRefreshTokenData requesterRefreshTokenData;

  /*Services Dto*/
  @Transient private LoginRequest loginRequest;
  @Transient private UpdateRole updateRole;
  @Transient private UpdateEmail updateEmail;
  @Transient private UpdatePassword updatePassword;

  public String getName() {
    return this.prsn.getName();
  }

  public void setName(String name) {
    this.prsn.setName(name);
  }

  public String getSurname() {
    return this.prsn.getSur();
  }

  public void setSurname(String surname) {
    this.prsn.setSur(surname);
  }

  public String getDescription() {
    return this.prsn.getProf().getDesc();
  }

  public String getAvatar() {
    return this.prsn.getProf().getAv();
  }

  public void setAndroidLastLogin(LocalDateTime time) {
    this.sys.getSec().getAndroidLog().setLLogin(time);
  }

  public void setIosLastLogin(LocalDateTime time) {
    this.sys.getSec().getIosLog().setLLogin(time);
  }

  public void setWebLastLogin(LocalDateTime time) {
    this.sys.getSec().getWebLog().setLLogin(time);
  }

  public void setAndroidLastLogout(LocalDateTime time) {
    this.sys.getSec().getAndroidLog().setLLogout(time);
  }

  public void setIosLastLogout(LocalDateTime time) {
    this.sys.getSec().getIosLog().setLLogout(time);
  }

  public void setWebLastLogout(LocalDateTime time) {
    this.sys.getSec().getWebLog().setLLogout(time);
  }

  public AndroidJWT getAndroidJwtModel() {
    return this.sys.getSec().getAndroidJWT();
  }

  public void setAndroidJwtModel(AndroidJWT androidJwtModel) {
    this.sys.getSec().setAndroidJWT(androidJwtModel);
  }

  public IosJWT getIosJwtModel() {
    return this.sys.getSec().getIosJWT();
  }

  public void setIosJwtModel(IosJWT iosJwtModel) {
    this.sys.getSec().setIosJWT(iosJwtModel);
  }

  public WebJWT getWebJwtModel() {
    return this.sys.getSec().getWebJWT();
  }

  public void setWebJwtModel(WebJWT webJwtModel) {
    this.sys.getSec().setWebJWT(webJwtModel);
  }

  public String getAndroidAccessTokenId() {
    return this.sys.getSec().getAndroidJWT().getAccToken().getId();
  }

  public void setAndroidAccessTokenId(String id) {
    this.sys.getSec().getAndroidJWT().getAccToken().setId(id);
  }

  public void setAndroidAccessToken(AccessToken accessToken) {
    this.sys.getSec().getAndroidJWT().setAccToken(accessToken);
  }

  public void setIosAccessToken(AccessToken accessToken) {
    this.sys.getSec().getIosJWT().setAccToken(accessToken);
  }

  public void setWebAccessToken(AccessToken accessToken) {
    this.sys.getSec().getWebJWT().setAccToken(accessToken);
  }

}
