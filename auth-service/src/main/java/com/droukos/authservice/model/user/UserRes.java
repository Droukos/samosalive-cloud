package com.droukos.authservice.model.user;

import com.droukos.authservice.environment.dto.client.auth.UpdateEmail;
import com.droukos.authservice.environment.dto.client.auth.UpdatePassword;
import com.droukos.authservice.environment.dto.client.auth.UpdateRole;
import com.droukos.authservice.environment.dto.client.auth.login.LoginRequest;
import com.droukos.authservice.model.user.personal.Address;
import com.droukos.authservice.model.user.personal.Personal;
import com.droukos.authservice.model.user.personal.PhoneList;
import com.droukos.authservice.model.user.personal.Profile;
import com.droukos.authservice.model.user.privacy.PrivacySettingMap;
import com.droukos.authservice.model.user.system.UserSystem;
import com.droukos.authservice.model.user.system.security.jwt.platforms.AndroidJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.IosJWT;
import com.droukos.authservice.model.user.system.security.jwt.platforms.WebJWT;
import com.droukos.authservice.model.user.system.security.jwt.tokens.AccessToken;
import com.droukos.authservice.model.user.system.security.jwt.tokens.RefreshToken;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.LocalDateTime;
import java.util.Date;
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
  @NonNull  private String userC;
  @NonNull  private String pass;
  @NonNull @Transient private String passC;
  @NonNull @Indexed private String email;
  @NonNull  private String emailC;
  @NonNull @Indexed private List<Role> allRoles;
  @NonNull  private Personal prsn;
  @NonNull  private PrivacySettingMap privy;
  @NonNull  private UserSystem sys;
  @NonNull  private AppState appState;

  /*Services Fields*/
  @Transient private ServerRequest serverRequest;
  @Transient private String userDevice;
  @Transient private String accessToken;
  @Transient private String refreshToken;
  @Transient private boolean refreshIsExpiring;

  /*Services Dto*/
  @Transient private LoginRequest loginRequest;
  @Transient private UpdateRole updateRole;
  @Transient private UpdateEmail updateEmail;
  @Transient private UpdatePassword updatePassword;

  public static void nullifyEmail(UserRes userRes) {
    userRes.setEmail(null);
    userRes.setEmailC(null);
  }

  public static void nullifyFullname(UserRes userRes) {
    userRes.setName(null);
    userRes.setSurname(null);
  }

  public static void nullifyUserCreation(UserRes userRes) {
    userRes.setAccountCreated(null);
  }

  public static void nullifyUserDescription(UserRes userRes) {
    userRes.setDescription(null);
  }

  public static void nullifyUserState(UserRes userRes) {
    userRes.getAppState().setOn(false);
  }

  public static void nullifyUserPhones(UserRes userRes) {
    userRes.setPhoneModel(null);
  }

  public static void nullifyLastLogin(UserRes userRes) {
    userRes.setAndroidLastLogin(null);
    userRes.setIosLastLogin(null);
    userRes.setWebLastLogin(null);
  }

  public static void nullifyLastLogout(UserRes userRes) {
    userRes.setAndroidLastLogout(null);
    userRes.setIosLastLogout(null);
    userRes.setWebLastLogout(null);
  }

  public static void nullifyAddress(UserRes userRes) {
    userRes.setCountryIso(null);
    userRes.setProvince(null);
    userRes.setCity(null);
    userRes.setAddressModel(null);
  }

  public Personal getPersonalModel() {
    return this.prsn;
  }

  public UserSystem getUserSystemModel() {
    return this.sys;
  }

  public Profile getProfileModel() {
    return this.prsn.getProf();
  }

  public PhoneList getPhoneModel() {
    return this.prsn.getPhoneList();
  }

  public void setPhoneModel(PhoneList phoneListModel) {
    this.prsn.setPhoneList(phoneListModel);
  }

  public Address getAddressModel() {
    return this.prsn.getAddr();
  }

  public void setAddressModel(Address addressModel) {
    this.prsn.setAddr(addressModel);
  }

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

  public void setDescription(String description) {
    this.prsn.getProf().setDesc(description);
  }

  public String getCountryIso() {
    return this.prsn.getAddr().getCIso();
  }

  public void setCountryIso(String countryIso) {
    this.prsn.getAddr().setCIso(countryIso);
  }

  public String getProvince() {
    return this.prsn.getAddr().getState();
  }

  public void setProvince(String state) {
    this.getPrsn().getAddr().setState(state);
  }

  public String getCity() {
    return this.prsn.getAddr().getCity();
  }

  public void setCity(String city) {
    this.getPrsn().getAddr().setCity(city);
  }

  public String getAvatar() {
    return this.prsn.getProf().getAv();
  }

  public void setAvatar(String avatarUrl) {
    this.prsn.getProf().setAv(avatarUrl);
  }

  public List<String> getPhones() {
    return this.prsn.getPhoneList().getPhones();
  }

  public void setPhones(List<String> phones) {
    this.prsn.getPhoneList().setPhones(phones);
  }

  public LocalDateTime getAndroidLastLogin() {
    return this.sys.getSec().getAndroidLog().getLLogin();
  }

  public void setAndroidLastLogin(LocalDateTime time) {
    this.sys.getSec().getAndroidLog().setLLogin(time);
  }

  public LocalDateTime getIosLastLogin() {
    return this.sys.getSec().getIosLog().getLLogin();
  }

  public void setIosLastLogin(LocalDateTime time) {
    this.sys.getSec().getIosLog().setLLogin(time);
  }

  public LocalDateTime getWebLastLogin() {
    return this.sys.getSec().getWebLog().getLLogin();
  }

  public void setWebLastLogin(LocalDateTime time) {
    this.sys.getSec().getWebLog().setLLogin(time);
  }

  public LocalDateTime getAndroidLastLogout() {
    return this.sys.getSec().getAndroidLog().getLLogout();
  }

  public void setAndroidLastLogout(LocalDateTime time) {
    this.sys.getSec().getAndroidLog().setLLogout(time);
  }

  public LocalDateTime getIosLastLogout() {
    return this.sys.getSec().getIosLog().getLLogout();
  }

  public void setIosLastLogout(LocalDateTime time) {
    this.sys.getSec().getIosLog().setLLogout(time);
  }

  public LocalDateTime getWebLastLogout() {
    return this.sys.getSec().getWebLog().getLLogout();
  }

  public void setWebLastLogout(LocalDateTime time) {
    this.sys.getSec().getWebLog().setLLogout(time);
  }

  public LocalDateTime getAccountCreated() {
    return this.sys.getAccC();
  }

  public void setAccountCreated(LocalDateTime time) {
    this.sys.setAccC(time);
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

  public RefreshToken getAndroidRefreshTokenModel() {
    return this.sys.getSec().getAndroidJWT().getReToken();
  }

  public void setAndroidRefreshTokenModel(RefreshToken refreshTokenModel) {
    this.sys.getSec().getAndroidJWT().setReToken(refreshTokenModel);
  }

  public RefreshToken getIosRefreshTokenModel() {
    return this.sys.getSec().getIosJWT().getReToken();
  }

  public void setIosRefreshTokenModel(RefreshToken refreshTokenModel) {
    this.sys.getSec().getIosJWT().setReToken(refreshTokenModel);
  }

  public RefreshToken getWebRefreshTokenModel() {
    return this.sys.getSec().getWebJWT().getReToken();
  }

  public void setWebRefreshTokenModel(RefreshToken refreshTokenModel) {
    this.sys.getSec().getWebJWT().setReToken(refreshTokenModel);
  }

  public String getAndroidRefreshTokenId() {
    return this.sys.getSec().getAndroidJWT().getReToken().getId();
  }

  public String getIosRefreshTokenId() {
    return this.sys.getSec().getIosJWT().getReToken().getId();
  }

  public String getWebRefreshTokenId() {
    return this.sys.getSec().getWebJWT().getReToken().getId();
  }

  public Date getAndroidRefreshTokenExp() {
    return this.sys.getSec().getAndroidJWT().getReToken().getExp();
  }

  public Date getIosRefreshTokenExp() {
    return this.sys.getSec().getIosJWT().getReToken().getExp();
  }

  public Date getWebRefreshTokenExp() {
    return this.sys.getSec().getWebJWT().getReToken().getExp();
  }

  public AccessToken getAndroidAccessTokenModel() {
    return this.sys.getSec().getAndroidJWT().getAccToken();
  }

  public AccessToken getIosAccessTokenModel() {
    return this.sys.getSec().getIosJWT().getAccToken();
  }

  public AccessToken getWebAccessTokenModel() {
    return this.sys.getSec().getWebJWT().getAccToken();
  }

  public String getAndroidAccessTokenId() {
    return this.sys.getSec().getAndroidJWT().getAccToken().getId();
  }

  public void setAndroidAccessTokenId(String id) {
    this.sys.getSec().getAndroidJWT().getAccToken().setId(id);
  }

  public String getIosAccessTokenId() {
    return this.sys.getSec().getIosJWT().getAccToken().getId();
  }

  public void setIosAccessTokenId(String id) {
    this.sys.getSec().getIosJWT().getAccToken().setId(id);
  }

  public String getWebAccessTokenId() {
    return this.sys.getSec().getWebJWT().getAccToken().getId();
  }

  public void setWebAccessTokenId(String id) {
    this.sys.getSec().getWebJWT().getAccToken().setId(id);
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
