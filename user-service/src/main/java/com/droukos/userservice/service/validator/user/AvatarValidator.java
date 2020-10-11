package com.droukos.userservice.service.validator.user;

import static com.droukos.userservice.environment.constants.Fields.AVATAR;
import static com.droukos.userservice.environment.enums.Warnings.AVATAR_INVALID;

import com.droukos.userservice.environment.dto.client.user.UpdateAvatar;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
      errors.reject(AVATAR, AVATAR_INVALID.getShortWarning());
    }
  }
}
