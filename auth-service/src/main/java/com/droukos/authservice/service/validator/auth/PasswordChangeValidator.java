package com.droukos.authservice.service.validator.auth;

import com.droukos.authservice.environment.dto.client.auth.UpdatePassword;
import com.droukos.authservice.environment.enums.Regexes;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.droukos.authservice.environment.constants.FieldNames.*;
import static com.droukos.authservice.environment.enums.Warnings.*;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

public class PasswordChangeValidator implements Validator {

  public static PasswordChangeValidator build() {
    return new PasswordChangeValidator();
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return UpdatePassword.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    rejectIfEmptyOrWhitespace(errors, F_PASS, PASSWORD_EMPTY.getShortWarning());
    rejectIfEmptyOrWhitespace(errors, F_PASSNEW, PASSWORD_NEW_EMPTY.getShortWarning());
    rejectIfEmptyOrWhitespace(errors, F_PASSNEWC, PASSWORD_NEW_CONFIRMED_EMPTY.getShortWarning());

    UpdatePassword updatePassword = (UpdatePassword) o;
    boolean passIsNotNull = updatePassword.getPass() != null;
    boolean passNewIsNotNull = updatePassword.getPassNew() != null;
    boolean passNewCIsNotNull = updatePassword.getPassNewC() != null;

    if ((passIsNotNull && passNewIsNotNull)
        && updatePassword.getPass().equals(updatePassword.getPassNew()))
      errors.rejectValue(F_PASSNEW, PASSWORD_PASSWORD_NEW_SAME.getShortWarning());

    if ((passNewIsNotNull && passNewCIsNotNull)
        && !updatePassword.getPassNew().equals(updatePassword.getPassNewC()))
      errors.rejectValue(F_PASSNEWC, PASSWORD_NEW_CONFIRMED_NOMATCH.getShortWarning());

    if (passNewIsNotNull
        && (updatePassword.getPassNew().length() > 160
            || !updatePassword.getPassNew().matches(Regexes.PASSWORD.getRegex())))
      errors.rejectValue(F_PASSNEW, PASSWORD_NEW_INVALID.getShortWarning());
  }
}
