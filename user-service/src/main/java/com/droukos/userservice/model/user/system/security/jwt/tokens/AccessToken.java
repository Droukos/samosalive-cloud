package com.droukos.userservice.model.user.system.security.jwt.tokens;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AccessToken {
  private String id;
}
