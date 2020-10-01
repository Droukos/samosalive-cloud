package com.droukos.authservice.service.validator.auth;

import com.droukos.authservice.environment.dto.client.auth.UpdateRole;
import com.droukos.authservice.util.RolesUtil;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.droukos.authservice.environment.constants.FieldNames.F_UPDATEDROLE;
import static com.droukos.authservice.environment.enums.Warnings.*;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

public class RoleDelValidator implements Validator {
  public static RoleDelValidator build() {
    return new RoleDelValidator();
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return UpdateRole.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    rejectIfEmptyOrWhitespace(errors, F_UPDATEDROLE, UPDATEDROLE_EMPTY.getShortWarning());

    UpdateRole updateRole = (UpdateRole) o;
    if (!RolesUtil.doesRoleExist(updateRole.getUpdatedRole()))
      errors.rejectValue(F_UPDATEDROLE, UPDATEDROLE_INVALID.getShortWarning());

    if (updateRole.getRolesOnDb().stream()
        .noneMatch(role -> role.equals(updateRole.getUpdatedRole())))
      errors.rejectValue(F_UPDATEDROLE, UPDATEDROLE_NOTEXISTS.getShortWarning());

    if (updateRole.getRolesOnDb().size() == 1)
      errors.rejectValue(F_UPDATEDROLE, UPDATEDROLE_LAST.getShortWarning());
  }
}
