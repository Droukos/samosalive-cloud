package com.droukos.authservice.model.user.system.security.jwt.tokens;

import lombok.*;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RefreshToken {
  private String id;
  private String ip;
  private Date exp;
}
