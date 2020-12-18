package com.droukos.authservice.environment.dto.client.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BanUser {
    private String userid;
    private long duration;
}
