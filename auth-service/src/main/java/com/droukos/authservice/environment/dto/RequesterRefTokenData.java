package com.droukos.authservice.environment.dto;

import lombok.Value;

import java.util.Date;

@Value
public class RequesterRefTokenData {
    String token;
    String tokenId;
    String userId;
    String username;
    String userDevice;
    Date expiration;
}
