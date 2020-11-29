package com.droukos.aedservice.environment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeviceAvailability {
    AVAILABLE(0),
    BROKEN(1),
    STOLEN(2),
    ON_REPAIR(3),
    BORROWED(4);
    private final int code;

}