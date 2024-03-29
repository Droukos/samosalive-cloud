package com.droukos.aedservice.environment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {

    OFFLINE(0),
    ONLINE(1),
    BUSY(2),
    ON_DUTY(3),
    AWAY(4);

    private final int code;
}
