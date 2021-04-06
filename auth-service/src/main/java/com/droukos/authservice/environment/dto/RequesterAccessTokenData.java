package com.droukos.authservice.environment.dto;

import lombok.Value;

import java.util.Date;
import java.util.List;


@Value
public class RequesterAccessTokenData {
    String token;
    String tokenId;
    String userId;
    String username;
    String userDevice;
    Date expiration;
    List<String> roles;
}
