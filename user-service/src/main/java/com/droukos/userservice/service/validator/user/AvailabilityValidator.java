package com.droukos.userservice.service.validator.user;

import static com.droukos.userservice.environment.constants.FieldNames.F_STATE_STATUS;
import static com.droukos.userservice.environment.enums.Warnings.EMPTY;
import static com.droukos.userservice.environment.enums.Warnings.INVALID;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

import com.droukos.userservice.environment.dto.client.user.UpdateAvailability;
import com.droukos.userservice.environment.enums.Availability;
import java.util.Arrays;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AvailabilityValidator implements Validator {

  public static AvailabilityValidator build() {
    return new AvailabilityValidator();
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return false;
  }

  @Override
  public void validate(Object o, Errors errors) {
    rejectIfEmptyOrWhitespace(errors, F_STATE_STATUS, F_STATE_STATUS + EMPTY.getShortWarning());

    UpdateAvailability updateAvailability = (UpdateAvailability) o;
    if (updateAvailability.getStatus() == null
        || Arrays.stream(Availability.values())
            .noneMatch(statusCode -> updateAvailability.getStatus() == statusCode.getCode())) {
      errors.rejectValue(F_STATE_STATUS, F_STATE_STATUS + INVALID.getShortWarning());
    }
  }
}
