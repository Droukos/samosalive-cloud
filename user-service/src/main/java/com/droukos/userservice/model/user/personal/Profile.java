package com.droukos.userservice.model.user.personal;

import com.droukos.userservice.environment.dto.client.user.UpdateUserPersonal;
import com.droukos.userservice.model.user.UserRes;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Profile {
  private String av;
  private String bk;
  private String desc;

  public static Profile updateProfileInfo(UserRes user, UpdateUserPersonal updateUserPersonal) {
    return new Profile(
            user.getAvatar(),
            user.getProfileModel().getBk(),
            updateUserPersonal.getDesc()
    );
  }
}
