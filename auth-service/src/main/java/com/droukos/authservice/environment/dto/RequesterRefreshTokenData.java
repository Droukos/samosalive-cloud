package com.droukos.authservice.environment.dto;

import lombok.*;

import java.util.Date;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RequesterRefreshTokenData {
    private String token;
    private String tokenId;
    private String userId;
    private String username;
    private String userDevice;
    private Date expiration;
    private boolean refreshIsExpiring;
}
