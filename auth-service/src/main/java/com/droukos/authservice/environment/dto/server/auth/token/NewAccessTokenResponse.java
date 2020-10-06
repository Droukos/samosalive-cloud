package com.droukos.authservice.environment.dto.server.auth.token;

import lombok.*;

@Data @ToString @Builder
@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
public class NewAccessTokenResponse {
    private String accessToken;
}
