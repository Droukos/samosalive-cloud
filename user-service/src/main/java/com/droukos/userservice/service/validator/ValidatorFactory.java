package com.droukos.userservice.service.validator;

import com.droukos.userservice.environment.dto.client.user.UpdateAvailability;
import com.droukos.userservice.environment.dto.client.user.UpdateUserPersonal;
import com.droukos.userservice.environment.dto.client.user.UpdateUserPrivacy;
import com.droukos.userservice.environment.enums.Regexes;
import com.droukos.userservice.service.validator.user.AvailabilityValidator;
import com.droukos.userservice.service.validator.user.PersonalValidator;
import com.droukos.userservice.service.validator.user.PrivacySettingsValidator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import static com.droukos.userservice.environment.security.HttpExceptionFactory.badRequest;
import static com.droukos.userservice.util.ValidatorUtil.validate;

@Component
public class ValidatorFactory {
    private ValidatorFactory(){}

    public static void validateUserPersonal(UpdateUserPersonal updateUserPersonal) {validate(updateUserPersonal, PersonalValidator.build());}

    public static boolean validateUsername(Tuple2<String, SecurityContext> tuple2) {
        return tuple2.getT1().matches(Regexes.VALIDNAME.getRegex());
    }

    public static Mono<String> validateUsername(String username) {
        return username != null && username.matches(Regexes.VALIDNAME.getRegex())
                ? Mono.just(username)
                : Mono.error(badRequest("Empty dto"));
    }

    public static void validateUserPrivacy(UpdateUserPrivacy updateUserPrivacy) {validate(updateUserPrivacy, PrivacySettingsValidator.build());}

    public static void validateUserAvailability(UpdateAvailability updateAvailability) { validate(updateAvailability, AvailabilityValidator.build());}
}
