package com.droukos.authservice.service.validator.auth;

import com.droukos.authservice.environment.dto.client.auth.UpdateEmail;
import com.droukos.authservice.environment.enums.Regexes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.droukos.authservice.environment.constants.FieldNames.F_NEWEMAIL;
import static com.droukos.authservice.environment.constants.FieldNames.F_PASS;
import static com.droukos.authservice.environment.enums.Warnings.*;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

public class EmailChangeValidator implements Validator {

  public static EmailChangeValidator build() {
    return new EmailChangeValidator();
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return false;
  }

  @Override
  public void validate(Object o, Errors errors) {
    rejectIfEmptyOrWhitespace(errors, F_PASS, PASSWORD_EMPTY.getShortWarning());
    rejectIfEmptyOrWhitespace(errors, F_NEWEMAIL, NEWMEMAIL_EMPTY.getShortWarning());

    UpdateEmail updateEmail = (UpdateEmail) o;
    if (updateEmail.getPass() != null
        && !new BCryptPasswordEncoder().matches(updateEmail.getPass(), updateEmail.getPassOnDB()))
      errors.rejectValue(F_PASS, USER_CREDENTIALS_INVALID.getShortWarning());

    if (updateEmail.getNewEmail() != null
        && updateEmail.getNewEmail().toLowerCase().trim().equals(updateEmail.getEmail()))
      errors.rejectValue(F_NEWEMAIL, NEWEMAIL_SAME.getShortWarning());

    if (updateEmail.getNewEmail() != null
        && (updateEmail.getNewEmail().length() > 160
            || !updateEmail.getNewEmail().matches(Regexes.EMAIL.getRegex())))
      errors.rejectValue(F_NEWEMAIL, NEWEMAIL_INVALID.getShortWarning());
  }
}
