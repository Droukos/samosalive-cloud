package com.droukos.authservice.service.validator.auth;

import com.droukos.authservice.environment.dto.client.auth.UpdateEmail;
import com.droukos.authservice.environment.dto.client.auth.UpdatePassword;
import com.droukos.authservice.environment.dto.client.auth.UpdateRole;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.service.validator.user.UserValidator;
import org.springframework.stereotype.Component;

import static com.droukos.authservice.util.ValidatorUtil.validate;

@Component
public class ValidatorFactory {
    private ValidatorFactory(){}
    public static void validateSignedUpUser(UserRes userRes) {validate(userRes, UserValidator.build());}
    public static void validateAddRole(UpdateRole updateRole) {validate(updateRole, RoleAddValidator.build());}
    public static void validateDelRole(UpdateRole updateRole) {validate(updateRole, RoleDelValidator.build());}
    public static void validateEmailChange(UpdateEmail updateEmail) {validate(updateEmail, EmailChangeValidator.build());}
    public static void validatePasswordChange(UpdatePassword updatePassword) {validate(updatePassword, PasswordChangeValidator.build());}
}
