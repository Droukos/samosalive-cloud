package com.droukos.userservice.environment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Roles {
    GENERAL_ADMIN(0, "GENERAL_ADMIN"),
    AREA_ADMIN(1, "AREA_ADMIN"),
    USER(2, "USER")
    ;

    private final int code;
    private final String role;
}
