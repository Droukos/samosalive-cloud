package com.droukos.authservice.model.user.system.security.jwt.platforms;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.environment.interfaces.JwtToken;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.model.user.system.security.jwt.tokens.AccessToken;
import com.droukos.authservice.model.user.system.security.jwt.tokens.RefreshToken;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class IosJWT implements JwtToken {
  RefreshToken reToken;
  AccessToken accToken;

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
    return accToken.getId();
  }

  @Override
  public String getRefreshTokenId() {
    return reToken.getId();
  }

  @Override
  public Date getRefreshTokenExp() {
    return reToken.getExp();
  }

  public static IosJWT noJwtUpdate(UserRes user) {
    return new IosJWT(
            RefreshToken.noUpdate(user.getSys().getSec().getIosJWT().reToken),
            AccessToken.noUpdate(user.getSys().getSec().getIosJWT().accToken)
    );
  }

  public static IosJWT jwtUpdate(NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {
    return new IosJWT(
            RefreshToken.update(refreshTokenData),
            AccessToken.update(accessTokenData)
    );
  }

  public static IosJWT jwtDeleteAccessToken(UserRes user) {
    return user.getIosJwtModel() != null && user.getIosJwtModel().getReToken() != null
            ? new IosJWT(user.getIosJwtModel().getReToken(), null)
            : null;
  }

  public static IosJWT jwtDeleteTokens() {
    return null;
  }

  public static IosJWT jwtUpdateAccessToken(UserRes user, NewAccTokenData tokenData) {
    return new IosJWT(user.getIosJwtModel().getReToken(), AccessToken.update(tokenData));
  }
}
