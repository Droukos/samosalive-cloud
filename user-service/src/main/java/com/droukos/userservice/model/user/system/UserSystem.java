package com.droukos.userservice.model.user.system;

import com.droukos.userservice.model.user.system.security.Security;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserSystem {

  private Double credStars;

  private LocalDateTime accC;

  private LocalDateTime accU;

  private Security sec;
}
