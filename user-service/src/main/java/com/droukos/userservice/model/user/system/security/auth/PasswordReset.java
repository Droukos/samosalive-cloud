package com.droukos.userservice.model.user.system.security.auth;

import lombok.*;

import java.time.LocalDateTime;

@Data @ToString
@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
public class PasswordReset {

    private String ip;
    private LocalDateTime date;

}
