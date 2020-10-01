package com.droukos.authservice.environment.dto.server.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor @AllArgsConstructor
public class RequestedUsernameOnly {
    private String user;
}
