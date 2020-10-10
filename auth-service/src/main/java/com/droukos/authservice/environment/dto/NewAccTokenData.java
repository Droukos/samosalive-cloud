package com.droukos.authservice.environment.dto;

import lombok.Value;

import java.util.Date;
import java.util.List;

@Value
public class NewAccTokenData {
    String token;
    String tokenId;
    String userId;
    String username;
    String userDevice;
    Date expiration;
    List<String> roles;

    public static NewAccTokenData nullAccessToken() {
        return new NewAccTokenData(null, null, null, null, null, null, null);
    }
}
