package com.droukos.authservice.environment.dto.client.auth;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UpdatePassword {
    private String pass;
    private String passNew;
    private String passNewC;
}
