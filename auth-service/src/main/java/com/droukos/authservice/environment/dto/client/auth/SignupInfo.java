package com.droukos.authservice.environment.dto.client.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class SignupInfo {
    private String username;
    private String password;
    private String passwordConfirmed;
    private String name;
    private String surname;
    private String email;
}
