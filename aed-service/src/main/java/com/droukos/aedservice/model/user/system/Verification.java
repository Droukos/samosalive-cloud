package com.droukos.aedservice.model.user.system;

import com.droukos.aedservice.model.user.UserRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Verification {

  boolean ver;
  LocalDateTime verOn;

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
