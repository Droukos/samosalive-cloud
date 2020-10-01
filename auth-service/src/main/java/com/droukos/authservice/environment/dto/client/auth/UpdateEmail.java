package com.droukos.authservice.environment.dto.client.auth;

import lombok.*;

@Data @ToString
@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
public class UpdateEmail {
    private String pass;
    private String passOnDB;
    private String email;
    private String newEmail;
}
