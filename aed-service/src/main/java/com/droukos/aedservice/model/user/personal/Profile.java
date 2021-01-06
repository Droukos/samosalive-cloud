package com.droukos.aedservice.model.user.personal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Profile {
  private String av;
  private String bk;
  private String desc;

  public static Profile withAvatarOnly(String avatar) {
    return new Profile(avatar, null, null);
  }

  public static Profile withBackgroundOnly(String background) {
    return new Profile(null, background, null);
  }

  public static Profile withDescriptionOnly(String description) {
    return new Profile(null, null, description);
  }

  public static Profile withoutDescription(String avatar, String background) {
    return new Profile(avatar, background, null);
  }

  public static Profile withoutBackground(String avatar, String description) {
    return new Profile(avatar, null, description);
  }

  public static Profile withoutAvatar(String background, String description) {
    return new Profile(null, background, description);
  }

}
