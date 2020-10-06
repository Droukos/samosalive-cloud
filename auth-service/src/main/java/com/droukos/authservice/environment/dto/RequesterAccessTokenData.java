package com.droukos.authservice.environment.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequesterAccessTokenData {
    private String token;
    private String tokenId;
    private String userId;
    private String username;
    private String userDevice;
    private Date expiration;
    private List<String> roles;
    private String pathVarId;
}
