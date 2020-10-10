package com.droukos.authservice.model.user.system.security.jwt.tokens;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import lombok.*;

@Value
public class AccessToken {
  String id;

  public static AccessToken noUpdate(AccessToken accessToken) {
    return new AccessToken(
            accessToken.id
    );
  }

  public static AccessToken update(NewAccTokenData newAccTokenData) {
    return new AccessToken(
            newAccTokenData.getTokenId()
    );
  }
}
