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
public class AndroidJWT implements JwtToken {
  RefreshToken reToken;
  AccessToken accToken;

  public static AndroidJWT noJwtUpdate(UserRes user) {
    return new AndroidJWT(
        RefreshToken.noUpdate(user.getSys().getSec().getAndroidJWT().getReToken()),
        AccessToken.noUpdate(user.getSys().getSec().getAndroidJWT().getAccToken()));
  }

  public static AndroidJWT jwtUpdate(NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {

    return new AndroidJWT(
        RefreshToken.update(refreshTokenData), AccessToken.update(accessTokenData));
  }

  public static AndroidJWT jwtDeleteAccessToken(UserRes user) {
    return user.getAndroidJwtModel() != null && user.getAndroidJwtModel().getReToken() != null
            ? new AndroidJWT(user.getAndroidJwtModel().getReToken(), null)
            : null;
  }

  public static AndroidJWT jwtDeleteTokens() {
    return null;
  }

  public static AndroidJWT jwtUpdateAccessToken(UserRes user, NewAccTokenData tokenData) {
    return new AndroidJWT(user.getAndroidJwtModel().getReToken(), AccessToken.update(tokenData));
  }

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
}
