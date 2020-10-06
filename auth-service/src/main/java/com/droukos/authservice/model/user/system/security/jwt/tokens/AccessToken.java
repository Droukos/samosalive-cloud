package com.droukos.authservice.model.user.system.security.jwt.tokens;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AccessToken {
  private String id;
}
