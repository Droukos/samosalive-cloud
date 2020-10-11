package com.droukos.userservice.service.validator.user;

import static com.droukos.userservice.environment.constants.Fields.USERNAME;
import static com.droukos.userservice.environment.enums.Warnings.USERNAME_EMPTY;
import static com.droukos.userservice.environment.enums.Warnings.USERNAME_INVALID;

import com.droukos.userservice.environment.enums.Regexes;
import com.droukos.userservice.model.user.UserRes;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UsernameOnlyValidator implements Validator {

  @Override
  public boolean supports(Class<?> aClass) {
    return UserRes.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, USERNAME, USERNAME_EMPTY.getShortWarning());

    String username = (String) o;
    if (username.matches(Regexes.VALIDNAME.getRegex())) errors.rejectValue(USERNAME, USERNAME_INVALID.getShortWarning());
  }
}
