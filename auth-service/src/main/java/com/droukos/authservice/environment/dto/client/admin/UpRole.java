package com.droukos.authservice.environment.dto.client.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpRole {
    private String username;
    private String role;
}
