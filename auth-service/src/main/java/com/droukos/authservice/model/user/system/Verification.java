package com.droukos.authservice.model.user.system;

import com.droukos.authservice.model.user.UserRes;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Verification {

  private boolean ver;
  private LocalDateTime verOn;

  public static Verification noUpdate(UserRes user) {
    return new Verification(
            user.getSys().getSec().getVerified().ver,
            user.getSys().getSec().getVerified().verOn
    );
  }

  public static Verification verifyUser() {
    return new Verification(
            true,
            LocalDateTime.now()
    );
  }
}
