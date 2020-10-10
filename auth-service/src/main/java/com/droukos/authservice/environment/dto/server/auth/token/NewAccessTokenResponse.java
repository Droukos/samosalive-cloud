package com.droukos.authservice.environment.dto.server.auth.token;

import lombok.*;

@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NewAccessTokenResponse {
  private String accessToken;
}
