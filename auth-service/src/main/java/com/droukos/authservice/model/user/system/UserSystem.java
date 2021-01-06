package com.droukos.authservice.model.user.system;

import com.droukos.authservice.model.user.system.security.Security;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UserSystem {

  private Double credStars;
  private LocalDateTime accC;
  private LocalDateTime accU;
  private Security sec;

}
