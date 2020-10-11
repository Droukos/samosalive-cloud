package com.droukos.userservice.service.validator.user;

import static com.droukos.userservice.environment.constants.FieldNames.F_PRIVY_ACCOUNT_CREATED;
import static com.droukos.userservice.environment.constants.FieldNames.F_PRIVY_ADDRESS;
import static com.droukos.userservice.environment.constants.FieldNames.F_PRIVY_DESCRIPTION;
import static com.droukos.userservice.environment.constants.FieldNames.F_PRIVY_EMAIL;
import static com.droukos.userservice.environment.constants.FieldNames.F_PRIVY_FULLNAME;
import static com.droukos.userservice.environment.constants.FieldNames.F_PRIVY_LAST_LOGIN;
import static com.droukos.userservice.environment.constants.FieldNames.F_PRIVY_LAST_LOGOUT;
import static com.droukos.userservice.environment.constants.FieldNames.F_PRIVY_ONLINE_STATUS;
import static com.droukos.userservice.environment.constants.FieldNames.F_PRIVY_PHONE;
import static com.droukos.userservice.environment.constants.FieldNames.F_PRIVY_TYPE;
import static com.droukos.userservice.environment.constants.FieldNames.F_PRIVY_USERS;
import static com.droukos.userservice.environment.enums.Regexes.VALIDNAME;
import static com.droukos.userservice.environment.enums.Warnings.PRIVY_EMPTY;
import static com.droukos.userservice.environment.enums.Warnings.PRIVY_INVALID_LENGTH;
import static com.droukos.userservice.environment.enums.Warnings.PRIVY_INVALID_TYPE;
import static com.droukos.userservice.environment.enums.Warnings.PRIVY_INVALID_USER;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

import com.droukos.userservice.environment.dto.client.user.UpdateUserPrivacy;
import com.droukos.userservice.environment.enums.PrivacySettings;
import com.droukos.userservice.model.user.privacy.PrivacySetting;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PrivacySettingsValidator implements Validator {
  public static PrivacySettingsValidator build() {
    return new PrivacySettingsValidator();
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return false;
  }

  @Override
  public void validate(Object o, Errors errors) {

    UpdateUserPrivacy update_userPrivacy = (UpdateUserPrivacy) o;
    List<String> types =
        Arrays.asList(
            F_PRIVY_ONLINE_STATUS,
            F_PRIVY_LAST_LOGIN,
            F_PRIVY_LAST_LOGOUT,
            F_PRIVY_ACCOUNT_CREATED,
            F_PRIVY_ADDRESS,
            F_PRIVY_FULLNAME,
            F_PRIVY_PHONE,
            F_PRIVY_DESCRIPTION,
            F_PRIVY_EMAIL);

    List<String> typeNames =
        types.stream().map(type -> type + F_PRIVY_TYPE).collect(Collectors.toList());
    List<String> usersNames =
        types.stream().map(type -> type + F_PRIVY_USERS).collect(Collectors.toList());

    typeNames.forEach(
        typeName ->
            rejectIfEmptyOrWhitespace(errors, typeName, typeName + PRIVY_EMPTY.getWarning()));

    List<PrivacySetting> privacySettings =
        Arrays.asList(
            update_userPrivacy.getOnStatus(),
            update_userPrivacy.getLstLogin(),
            update_userPrivacy.getLstLogout(),
            update_userPrivacy.getAccC(),
            update_userPrivacy.getAddr(),
            update_userPrivacy.getName(),
            update_userPrivacy.getPhone(),
            update_userPrivacy.getDesc(),
            update_userPrivacy.getEmail());
    List<Integer> typeCodes =
        privacySettings.stream()
            .filter(e -> e.getType() != null)
            .map(PrivacySetting::getType)
            .collect(Collectors.toList());
    List<List<String>> users =
        privacySettings.stream()
            .filter(e -> e.getUsers() != null)
            .map(PrivacySetting::getUsers)
            .collect(Collectors.toList());

    IntStream.range(0, typeCodes.size())
        .boxed()
        .collect(Collectors.toMap(typeNames::get, typeCodes::get))
        .forEach(
            (typeName, typeCode) -> {
              if (Arrays.stream(PrivacySettings.values())
                  .noneMatch(privacySetting -> privacySetting.code() == typeCode))
                errors.rejectValue(typeName, typeName + PRIVY_INVALID_TYPE.getWarning());
            });

    IntStream.range(0, users.size())
        .boxed()
        .collect(Collectors.toMap(usersNames::get, users::get))
        .forEach(
            (userName, usersList) -> {
              if (usersList.size() > 100)
                errors.rejectValue(userName, userName + PRIVY_INVALID_LENGTH.getWarning());
              else if (usersList.stream()
                  .anyMatch(user -> user.length() > 30 || !user.matches(VALIDNAME.getRegex())))
                errors.rejectValue(userName, userName + PRIVY_INVALID_USER.getWarning());
            });
  }
}
