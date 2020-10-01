package com.droukos.authservice.service.validator.user;

import com.droukos.authservice.model.user.UserRes;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static com.droukos.authservice.environment.constants.Fields.USERNAME;
import static com.droukos.authservice.environment.enums.Warnings.USERNAME_EMPTY;

public class UsernameOnlyValidator implements Validator {

  @Override
  public boolean supports(Class<?> aClass) {
    return UserRes.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, USERNAME, USERNAME_EMPTY.getShortWarning());

  }
}
