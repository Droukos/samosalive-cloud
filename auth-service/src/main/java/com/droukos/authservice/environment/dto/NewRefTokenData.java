package com.droukos.authservice.environment.dto;

import lombok.Value;

import java.util.Date;

@Value
public class NewRefTokenData {
    String token;
    String tokenId;
    String ip;
    String userId;
    String username;
    String userDevice;
    Date expiration;

    public static NewRefTokenData nullRefreshToken() {
        return new NewRefTokenData(null, null, null, null ,null, null, null);
    }
}
