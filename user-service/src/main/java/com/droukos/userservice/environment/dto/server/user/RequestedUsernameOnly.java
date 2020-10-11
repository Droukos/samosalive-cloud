package com.droukos.userservice.environment.dto.server.user;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor @AllArgsConstructor
public class RequestedUsernameOnly {
    private String user;
}
