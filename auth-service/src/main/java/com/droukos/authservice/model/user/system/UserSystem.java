package com.droukos.authservice.model.user.system;

import com.droukos.authservice.model.user.system.security.Security;
import lombok.*;

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
