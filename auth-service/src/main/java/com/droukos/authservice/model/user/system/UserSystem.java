package com.droukos.authservice.model.user.system;

import com.droukos.authservice.model.user.system.security.Security;
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

  private Double credStars;
  private LocalDateTime accC;
  private LocalDateTime accU;
  private Security sec;

}
