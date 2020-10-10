package com.droukos.authservice.service.validator.user;

import com.droukos.authservice.environment.dto.client.auth.SignupInfo;
import com.droukos.authservice.environment.enums.Regexes;
import com.droukos.authservice.model.user.UserRes;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.droukos.authservice.environment.constants.Fields.*;
import static com.droukos.authservice.environment.enums.Regexes.VALIDNAME;
import static com.droukos.authservice.environment.enums.Warnings.*;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

public class UserValidator implements Validator {

  public static UserValidator build() {
    return new UserValidator();
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return UserRes.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    rejectIfEmptyOrWhitespace(errors, USERNAME, USERNAME_EMPTY.getShortWarning());
    rejectIfEmptyOrWhitespace(errors, PASSWORD, PASSWORD_EMPTY.getShortWarning());
    rejectIfEmptyOrWhitespace(
        errors, PASSWORD_CONFRIMED, PASSWORD_CONFIRMED_EMPTY.getShortWarning());
    rejectIfEmptyOrWhitespace(errors, EMAIL, EMAIL_EMPTY.getShortWarning());
    rejectIfEmptyOrWhitespace(errors, NAME, NAME_EMPTY.getShortWarning());
    rejectIfEmptyOrWhitespace(errors, SURNAME, SURNAME_EMPTY.getShortWarning());

    SignupInfo signupInfo = (SignupInfo) o;

    checkValues(signupInfo, errors);
  }

  private void checkValues(SignupInfo signupInfo, Errors errors) {
    if (!signupInfo.getUsername().matches(VALIDNAME.getRegex()))
      errors.rejectValue(USERNAME, USERNAME_INVALID.getShortWarning());
    if (!signupInfo.getName().matches(VALIDNAME.getRegex()))
      errors.rejectValue(NAME, NAME_INVALID.getShortWarning());
    if (!signupInfo.getSurname().matches(VALIDNAME.getRegex()))
      errors.rejectValue(SURNAME, SURNAME_INVALID.getShortWarning());
    if (signupInfo.getPassword().length() > 160 || !signupInfo.getPassword().matches(Regexes.PASSWORD.getRegex()))
      errors.rejectValue(PASSWORD, PASSWORD_INVALID.getShortWarning());
    if (!signupInfo.getPassword().equals(signupInfo.getPasswordConfirmed()))
      errors.rejectValue(PASSWORD_CONFRIMED, PASSWORD_NOMATCH_ERROR.getShortWarning());
    if (signupInfo.getEmail().length() > 160 || !signupInfo.getEmail().matches(Regexes.EMAIL.getRegex()))
      errors.rejectValue(EMAIL, EMAIL_INVALID.getShortWarning());
  }
}
