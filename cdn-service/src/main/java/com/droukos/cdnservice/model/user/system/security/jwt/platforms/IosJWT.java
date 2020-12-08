package com.droukos.cdnservice.model.user.system.security.jwt.platforms;

import com.droukos.cdnservice.environment.interfaces.JwtToken;
import com.droukos.cdnservice.model.user.system.security.jwt.tokens.AccessToken;
import com.droukos.cdnservice.model.user.system.security.jwt.tokens.RefreshToken;
import lombok.*;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
