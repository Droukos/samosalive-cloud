package com.droukos.authservice.environment.dto.client.auth.login;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private String user;
    private String pass;

}
