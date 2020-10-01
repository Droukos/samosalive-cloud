package com.droukos.authservice.environment.dto.server.auth.token;

import lombok.*;

@Data @ToString
@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
public class NewAccessTokenResponse {
    private String accessToken;
}
