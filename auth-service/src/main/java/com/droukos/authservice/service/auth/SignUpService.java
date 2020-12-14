package com.droukos.authservice.service.auth;

import com.droukos.authservice.environment.constants.FieldNames;
import com.droukos.authservice.environment.dto.client.auth.CheckUniqueness;
import com.droukos.authservice.environment.dto.client.auth.SignupInfo;
import com.droukos.authservice.model.user.AppState;
import com.droukos.authservice.model.user.ChannelSubs;
import com.droukos.authservice.model.user.RoleModel;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.personal.AddressModel;
import com.droukos.authservice.model.user.personal.Personal;
import com.droukos.authservice.model.user.personal.Profile;
import com.droukos.authservice.model.user.privacy.PrivacySetting;
import com.droukos.authservice.model.user.privacy.PrivacySettingMap;
import com.droukos.authservice.model.user.system.UserSystem;
import com.droukos.authservice.model.user.system.Verification;
import com.droukos.authservice.model.user.system.security.Security;
import com.droukos.authservice.model.user.system.security.logins.AndroidLogins;
import com.droukos.authservice.model.user.system.security.logins.IosLogins;
import com.droukos.authservice.model.user.system.security.logins.WebLogins;
import com.droukos.authservice.repo.UserRepository;
import com.droukos.authservice.util.factories.HttpExceptionFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;

import static com.droukos.authservice.environment.constants.authorities.Roles.SYSTEM;
import static com.droukos.authservice.environment.constants.authorities.Roles.USER;
import static com.droukos.authservice.environment.enums.Availability.OFFLINE;
import static com.droukos.authservice.environment.enums.PrivacySettings.PRIVATE;
import static com.droukos.authservice.environment.enums.PrivacySettings.PUBLIC;

@Service
@RequiredArgsConstructor
public class SignUpService {

  @NonNull private final UserRepository userRepository;
  @NonNull private final BCryptPasswordEncoder passwordEncoder;

  public static PrivacySettingMap generateUserInfoSettingsPrivacy() {
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

  public static UserSystem generateUserInfoSystem() {

    return new UserSystem(
        5.0,
        LocalDateTime.now(),
        LocalDateTime.now(),
        new Security(
            new AndroidLogins(null, null),
            new IosLogins(null, null),
            new WebLogins(null, null),
            null,
            null,
            null,
            null,
            new Verification(false, null),
            null,
            null));
  }

  public Mono<Boolean> signedUpUser(UserRes user) {
    return userRepository.save(user).flatMap(savedUser -> Mono.just(true));
  }

  public Mono<Boolean> checkUsernameUniqueness(CheckUniqueness checkUniqueness) {
    return userRepository
        .findFirstByUser(checkUniqueness.getUser().toLowerCase())
        // .filter(duplicatedUser -> duplicatedUser.getId() != null)
        .defaultIfEmpty(new UserRes())
        .flatMap(user -> user.getId() != null ? Mono.just(false) : Mono.just(true));
    // .then(duplicatedUser -> new BadResponses().emailTaken())
  }

  public Mono<Boolean> checkEmailUniqueness(CheckUniqueness checkUniqueness) {
    return userRepository
        .findFirstByEmail(checkUniqueness.getUser().toLowerCase())
        .defaultIfEmpty(new UserRes())
        .flatMap(user -> user.getId() != null ? Mono.just(false) : Mono.just(true));
  }

  public Mono<SignupInfo> checkUsernameEmailUniqueness(SignupInfo signupInfo) {
    return userRepository
        .findFirstByUserOrEmail(signupInfo.getUsername().toLowerCase(), signupInfo.getEmail().toLowerCase())
        .defaultIfEmpty(new UserRes())
        .flatMap(
            existedUser ->
                existedUser.getId() != null
                    ? Mono.error(HttpExceptionFactory.badRequest())
                    : Mono.just(signupInfo));
  }

  public Mono<Boolean> notFoundUsername(UserRes user) {
    return Mono.just(true);
  }

  public Mono<Boolean> notFoundEmail(UserRes user) {
    return Mono.just(true);
  }

  public Mono<UserRes> buildUser(SignupInfo signupInfo) {
    return Mono.just(
        new UserRes(
            null,
            signupInfo.getUsername().toLowerCase(),
            signupInfo.getUsername(),
            passwordEncoder.encode(signupInfo.getPassword()),
            signupInfo.getPasswordConfirmed(),
            signupInfo.getEmail().toLowerCase(),
            signupInfo.getEmail(),
            Collections.singletonList(new RoleModel(USER, null, true, LocalDateTime.now(), SYSTEM)),
            generateUserInfoPersonal(signupInfo.getName(), signupInfo.getSurname()),
            generateUserInfoSettingsPrivacy(),
            generateUserInfoSystem(),
            new ChannelSubs(),
            new AppState(false, OFFLINE.getCode())));
  }

  private Personal generateUserInfoPersonal(String name, String surname) {
    return new Personal(
        name, surname, new Profile(null, null, null), new AddressModel(null, null, null), null);
  }
}
