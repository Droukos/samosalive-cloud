package com.droukos.userservice.model.user.system.security.jwt.platforms;

import com.droukos.userservice.environment.interfaces.JwtToken;
import com.droukos.userservice.model.user.system.security.jwt.tokens.AccessToken;
import com.droukos.userservice.model.user.system.security.jwt.tokens.RefreshToken;
import lombok.*;

import java.util.Date;

@Data @ToString
@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
public class WebJWT implements JwtToken {
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
