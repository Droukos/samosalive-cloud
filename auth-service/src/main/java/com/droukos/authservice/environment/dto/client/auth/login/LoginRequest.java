package com.droukos.authservice.environment.dto.client.auth.login;

import lombok.*;
import org.springframework.web.reactive.function.server.ServerRequest;

@Data @ToString @Getter
@AllArgsConstructor @NoArgsConstructor
public class LoginRequest {

    private String user;
    private String pass;
    private ServerRequest request;

}
