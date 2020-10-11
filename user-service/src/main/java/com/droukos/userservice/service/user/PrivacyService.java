package com.droukos.userservice.service.user;

import static com.droukos.userservice.util.ValidatorUtil.validate;

import com.droukos.userservice.environment.constants.FieldNames;
import com.droukos.userservice.environment.constants.StatusCodes;
import com.droukos.userservice.environment.dto.client.user.UpdateUserPrivacy;
import com.droukos.userservice.environment.dto.server.ApiResponse;
import com.droukos.userservice.model.user.UserRes;
import com.droukos.userservice.model.user.privacy.PrivacySetting;
import com.droukos.userservice.model.user.privacy.PrivacySettingMap;
import com.droukos.userservice.repo.UserRepository;
import com.droukos.userservice.service.validator.user.PrivacySettingsValidator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Lazy
@RequiredArgsConstructor
public class PrivacyService {

  /*@NonNull private final UserRepository userRepository;

  public Mono<User> validatePrivacySets(UserRes user) {
    Function<UpdateUserPrivacy, Map<String, PrivacySetting>> privacyMapSup = updateUserPrivacy -> {
        var map = new HashMap<String, PrivacySetting>();
        map.put(FieldNames.F_PRIVY_ONLINE_STATUS, filterPrivacySetting(updateUserPrivacy.getOnStatus()));
        map.put(FieldNames.F_PRIVY_LAST_LOGIN, filterPrivacySetting(updateUserPrivacy.getLstLogin()));
        map.put(FieldNames.F_PRIVY_LAST_LOGOUT, filterPrivacySetting(updateUserPrivacy.getLstLogout()));
        map.put(FieldNames.F_PRIVY_FULLNAME, filterPrivacySetting(updateUserPrivacy.getName()));
        map.put(FieldNames.F_PRIVY_EMAIL, filterPrivacySetting(updateUserPrivacy.getEmail()));
        map.put(FieldNames.F_PRIVY_ACCOUNT_CREATED, filterPrivacySetting(updateUserPrivacy.getAccC()));
        map.put(FieldNames.F_PRIVY_DESCRIPTION, filterPrivacySetting(updateUserPrivacy.getDesc()));
        map.put(FieldNames.F_PRIVY_ADDRESS, filterPrivacySetting(updateUserPrivacy.getAddr()));
        map.put(FieldNames.F_PRIVY_PHONE, filterPrivacySetting(updateUserPrivacy.getPhone()));
        return map;
    };

    Consumer<UpdateUserPrivacy> prepareUpdate = updateUserPrivacy ->
        user.setPrivy(new Privacy_Settings(privacyMapSup.apply(updateUserPrivacy)));

    Function<UpdateUserPrivacy, Mono<User>> process = updateUserPrivacy -> {
      validate(updateUserPrivacy, PrivacySettingsValidator.build());
      prepareUpdate.accept(updateUserPrivacy);
      return Mono.just(user);
    };
    return request
        .bodyToMono(UpdateUserPrivacy.class)
        .defaultIfEmpty(new UpdateUserPrivacy())
        .flatMap(process);
  }

  private PrivacySetting filterPrivacySetting(PrivacySetting privacySetting) {
    if (privacySetting == null || privacySetting.getUsers() == null) {
      return privacySetting;
    }
    privacySetting.setUsers(
        privacySetting.getUsers().stream().map(String::toLowerCase).collect(Collectors.toList()));
    return privacySetting;
  }

  public Mono<ServerResponse> updatePrivacySettings(User user) {
    Function<User, Mono<ServerResponse>> result = savedUser -> ok().body(BodyInserters.fromValue(
        new ApiResponse(StatusCodes.OK, "User Privacy Settings Updated", "user.privacy.updated")));

    return userRepository.save(user)
        .flatMap(result);
  }*/
}
