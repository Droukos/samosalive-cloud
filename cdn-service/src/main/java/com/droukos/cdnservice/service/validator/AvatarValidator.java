package com.droukos.cdnservice.service.validator;

import com.droukos.cdnservice.environment.dto.client.UpdateAvatar;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.imageio.ImageIO;

import static com.droukos.cdnservice.environment.constants.Fields.AVATAR;

@Component
public class AvatarValidator implements Validator {

    public static AvatarValidator build() {
        return new AvatarValidator();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UpdateAvatar.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        // ValidationUtils.rejectIfEmptyOrWhitespace(errors, "av",
        // AVATAR_INVALID.getShortWarning());

        UpdateAvatar update_avatar = (UpdateAvatar) o;
        try {
            // It's an image (only BMP, GIF, JPG and PNG are recognized).
            ImageIO.read(update_avatar.getAv()).toString();
        } catch (Exception e) {
            errors.reject(AVATAR, "invalid.file");
        }
    }
}
