package com.droukos.authservice.util;

import com.droukos.authservice.environment.constants.FieldNames;
import com.droukos.authservice.environment.dto.client.auth.SignupInfo;
import com.droukos.authservice.environment.interfaces.user_creation.*;
import com.droukos.authservice.model.user.AppState;
import com.droukos.authservice.model.user.Role;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.personal.Address;
import com.droukos.authservice.model.user.personal.Personal;
import com.droukos.authservice.model.user.personal.PhoneList;
import com.droukos.authservice.model.user.personal.Profile;
import com.droukos.authservice.model.user.privacy.PrivacySetting;
import com.droukos.authservice.model.user.privacy.PrivacySettingMap;
import com.droukos.authservice.model.user.system.UserSystem;
import com.droukos.authservice.model.user.system.Verification;
import com.droukos.authservice.model.user.system.security.Security;
import com.droukos.authservice.model.user.system.security.logins.AndroidLogins;
import com.droukos.authservice.model.user.system.security.logins.IosLogins;
import com.droukos.authservice.model.user.system.security.logins.WebLogins;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;

import static com.droukos.authservice.environment.enums.Availability.OFFLINE;
import static com.droukos.authservice.environment.enums.PrivacySettings.PRIVATE;
import static com.droukos.authservice.environment.enums.PrivacySettings.PUBLIC;
import static com.droukos.authservice.environment.roles.roles_list.lvl1_roles.UserRoles.USER;

@RequiredArgsConstructor
public class UserGenUtil
    implements UsernameCreation,
        PasswordCreation,
        NameCreation,
        SurnameCreation,
        EmailCreation,
        BuilderCreation {

  private static final String SYSTEM = "SYSTEM";
  @NonNull private final BCryptPasswordEncoder passwordEncoder;
  private String username;
  private String password;
  private String passwordConfirmed;
  private String email;
  private String name;
  private String surname;

  public static UserRes buildUserFromSignupInfoDto(
      BCryptPasswordEncoder passwordEncoder, SignupInfo signupInfo) {
    return new UserGenUtil(passwordEncoder)
        .username(signupInfo.getUsername())
        .passwordWithPassConf(signupInfo.getPassword(), signupInfo.getPasswordConfirmed())
        .name(signupInfo.getName())
        .surname(signupInfo.getSurname())
        .email(signupInfo.getEmail())
        .build();
  }

  @Override
  public BuilderCreation email(String email) {
    this.email = email;
    return this;
  }

  @Override
  public SurnameCreation name(String name) {
    this.name = name;
    return this;
  }

  @Override
  public EmailCreation surname(String surname) {
    this.surname = surname;
    return this;
  }

  @Override
  public PasswordCreation username(String username) {
    this.username = username;
    return this;
  }

  @Override
  public NameCreation password(String password) {
    this.password = password;
    return this;
  }

  @Override
  public NameCreation passwordWithPassConf(String password, String passwordConfirmed) {
    this.password = password;
    this.passwordConfirmed = passwordConfirmed;
    return this;
  }

  @Override
  public Mono<UserRes> buildMono() {
    return Mono.just(
        new UserRes(
            username.toLowerCase(),
            username,
            passwordEncoder.encode(password),
            password,
            email.toLowerCase(),
            email,
            Collections.singletonList(new Role(USER.getCode(), true, LocalDateTime.now(), SYSTEM)),
            generateUserInfoPersonal(name, surname),
            generateUserInfoSettingsPrivacy(),
            generateUserInfoSystem(),
            new AppState(false, OFFLINE.getCode())));
  }

  @Override
  public UserRes build() {
    return new UserRes(
        username.toLowerCase(),
        username,
        password,
        passwordConfirmed,
        email.toLowerCase(),
        email,
        Collections.singletonList(new Role(USER.getCode(), true, LocalDateTime.now(), SYSTEM)),
        generateUserInfoPersonal(name, surname),
        generateUserInfoSettingsPrivacy(),
        generateUserInfoSystem(),
        new AppState(false, OFFLINE.getCode()));
  }

  private Personal generateUserInfoPersonal(String name, String surname) {
    return new Personal(
        name,
        surname,
        new Profile(null, null, null),
        new Address(null, null, null),
        new PhoneList(null));
  }

  private PrivacySettingMap generateUserInfoSettingsPrivacy() {
    var map = new HashMap<String, PrivacySetting>();
    map.put(FieldNames.F_PRIVY_ONLINE_STATUS, new PrivacySetting(PUBLIC.code(), null));
    map.put(FieldNames.F_PRIVY_LAST_LOGIN, new PrivacySetting(PRIVATE.code(), null));
    map.put(FieldNames.F_PRIVY_LAST_LOGOUT, new PrivacySetting(PRIVATE.code(), null));
    map.put(FieldNames.F_PRIVY_FULLNAME, new PrivacySetting(PUBLIC.code(), null));
    map.put(FieldNames.F_PRIVY_EMAIL, new PrivacySetting(PRIVATE.code(), null));
    map.put(FieldNames.F_PRIVY_ACCOUNT_CREATED, new PrivacySetting(PUBLIC.code(), null));
    map.put(FieldNames.F_PRIVY_DESCRIPTION, new PrivacySetting(PUBLIC.code(), null));
    map.put(FieldNames.F_PRIVY_ADDRESS, new PrivacySetting(PRIVATE.code(), null));
    map.put(FieldNames.F_PRIVY_PHONE, new PrivacySetting(PRIVATE.code(), null));
    return new PrivacySettingMap(map);
  }

  private UserSystem generateUserInfoSystem() {
    return new UserSystem(
        5.0,
        LocalDateTime.now(),
        LocalDateTime.now(),
        new Security(
            new AndroidLogins(),
            new IosLogins(),
            new WebLogins(),
            null,
            null,
            null,
            null,
            new Verification(false, null),
            null,
            null));
  }
}
