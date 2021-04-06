package com.droukos.authservice.model.user.system.security.jwt.tokens;

import com.droukos.authservice.environment.dto.NewRefTokenData;
import lombok.Value;

import java.util.Date;

@Value
public class RefreshToken {
  String rid;
  String ip;
  Date exp;

  public static RefreshToken noUpdate(RefreshToken refreshToken) {
    return new RefreshToken(
            refreshToken.rid,
            refreshToken.ip,
            refreshToken.exp
    );
  }

  public static RefreshToken update(NewRefTokenData newRefTokenData) {
    return new RefreshToken(
            newRefTokenData.getTokenId(),
            newRefTokenData.getIp(),
            newRefTokenData.getExpiration()
    );
  }
}
