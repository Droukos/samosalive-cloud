package com.droukos.authservice.service.validator.auth;

import com.droukos.authservice.environment.dto.client.auth.UpdateRole;
import com.droukos.authservice.util.RolesUtil;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.droukos.authservice.environment.constants.FieldNames.F_UPDATEDROLE;
import static com.droukos.authservice.environment.enums.Warnings.*;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

public class RoleAddValidator implements Validator {
  public static RoleAddValidator build() {
    return new RoleAddValidator();
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
        .anyMatch(roleCode -> roleCode.equals(updateRole.getUpdatedRole())))
      errors.rejectValue(F_UPDATEDROLE, UPDATEDROLE_EXISTS.getShortWarning());
  }
}
