package com.droukos.userservice.service.validator.user;

import static com.droukos.userservice.environment.constants.FieldNames.F_CITY;
import static com.droukos.userservice.environment.constants.FieldNames.F_COUNTRYISO;
import static com.droukos.userservice.environment.constants.FieldNames.F_DESCRIPTION;
import static com.droukos.userservice.environment.constants.FieldNames.F_NAME;
import static com.droukos.userservice.environment.constants.FieldNames.F_STATE;
import static com.droukos.userservice.environment.constants.FieldNames.F_SURNAME;
import static com.droukos.userservice.environment.enums.Regexes.VALIDNAME;
import static com.droukos.userservice.environment.enums.Warnings.CITY_INVALID_LENGTH;
import static com.droukos.userservice.environment.enums.Warnings.COUNTRYISO_INVALID;
import static com.droukos.userservice.environment.enums.Warnings.DESCRIPTION_INVALID_LENGTH;
import static com.droukos.userservice.environment.enums.Warnings.NAME_EMPTY;
import static com.droukos.userservice.environment.enums.Warnings.NAME_INVALID;
import static com.droukos.userservice.environment.enums.Warnings.STATE_INVALID_LENGTH;
import static com.droukos.userservice.environment.enums.Warnings.SURNAME_EMPTY;
import static com.droukos.userservice.environment.enums.Warnings.SURNAME_INVALID;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

import com.droukos.userservice.environment.dto.client.user.UpdateUserPersonal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PersonalValidator implements Validator {

  public static boolean isValidISOCountry(String s) {
    if (s.length() > 2) return false;
    return new HashSet<>(Arrays.asList(Locale.getISOCountries())).contains(s.toUpperCase());
  }

  public static PersonalValidator build() {
    return new PersonalValidator();
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return UpdateUserPersonal.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    rejectIfEmptyOrWhitespace(errors, F_NAME, NAME_EMPTY.getShortWarning());
    rejectIfEmptyOrWhitespace(errors, F_SURNAME, SURNAME_EMPTY.getShortWarning());

    UpdateUserPersonal userInfoPersonal = (UpdateUserPersonal) o;
    if (userInfoPersonal.getName() == null
        || !userInfoPersonal.getName().matches(VALIDNAME.getRegex()))
      errors.rejectValue(F_NAME, NAME_INVALID.getShortWarning());

    if (userInfoPersonal.getSur() == null
        || !userInfoPersonal.getSur().matches(VALIDNAME.getRegex()))
      errors.rejectValue(F_SURNAME, SURNAME_INVALID.getShortWarning());

    if (userInfoPersonal.getDesc() != null && userInfoPersonal.getDesc().length() > 250)
      errors.rejectValue(F_DESCRIPTION, DESCRIPTION_INVALID_LENGTH.getShortWarning());

    if (userInfoPersonal.getCiso() != null && !isValidISOCountry(userInfoPersonal.getCiso()))
      errors.rejectValue(F_COUNTRYISO, COUNTRYISO_INVALID.getShortWarning());

    if (userInfoPersonal.getState() != null && userInfoPersonal.getState().length() > 40)
      errors.rejectValue(F_STATE, STATE_INVALID_LENGTH.getShortWarning());

    if (userInfoPersonal.getCity() != null && userInfoPersonal.getCity().length() > 40)
      errors.rejectValue(F_CITY, CITY_INVALID_LENGTH.getShortWarning());
  }
}
