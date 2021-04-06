package com.droukos.userservice.model.user.system.security.jwt.tokens;

import lombok.*;

import java.util.Date;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RefreshToken {
  private String rid;
  private String ip;
  private Date exp;
}
