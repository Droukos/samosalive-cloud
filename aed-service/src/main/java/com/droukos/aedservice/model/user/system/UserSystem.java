package com.droukos.aedservice.model.user.system;

import com.droukos.aedservice.model.user.system.security.Security;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UserSystem {

  Double credStars;
  LocalDateTime accC;
  LocalDateTime accU;
  Security sec;

}
