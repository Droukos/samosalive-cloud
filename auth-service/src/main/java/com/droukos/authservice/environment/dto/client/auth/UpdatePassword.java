package com.droukos.authservice.environment.dto.client.auth;

import com.droukos.authservice.model.user.UserRes;
import lombok.*;

@Data @ToString
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class UpdatePassword {
    private UserRes user;
    private String pass;
    private String passOnDb;
    private String passNew;
    private String passNewC;
}
