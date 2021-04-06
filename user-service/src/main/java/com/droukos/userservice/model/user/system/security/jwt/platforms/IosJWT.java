package com.droukos.userservice.model.user.system.security.jwt.platforms;

import com.droukos.userservice.environment.interfaces.JwtToken;
import com.droukos.userservice.model.user.system.security.jwt.tokens.AccessToken;
import com.droukos.userservice.model.user.system.security.jwt.tokens.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class IosJWT implements JwtToken {
  private RefreshToken reToken;
  private AccessToken accToken;

  @Override
  public AccessToken getAccessTokenModel() {
    return accToken;
  }

  @Override
  public RefreshToken getRefreshTokenModel() {
    return reToken;
  }

  @Override
  public String getAccessTokenId() {
    return accToken.getAid();
  }

  @Override
  public String getRefreshTokenId() {
    return reToken.getRid();
  }

  @Override
  public Date getRefreshTokenExp() {
    return reToken.getExp();
  }
}
