package com.droukos.cdnservice.model.user.system.security.logins;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AndroidLogins {
    private LocalDateTime lLogin;
    private LocalDateTime lLogout;
}
