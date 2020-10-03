package com.droukos.authservice.environment.dto.client.auth.login;

import lombok.*;
import org.springframework.web.reactive.function.server.ServerRequest;

@Data @ToString @Getter
@RequiredArgsConstructor @NoArgsConstructor
public class LoginRequest {

    @NonNull private String user;
    @NonNull private String pass;
    private ServerRequest request;

}
