package com.droukos.aedservice.environment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeviceAvailability {
    AVAILABLE(0),
    UNAVAILABLE(1),
    //BROKEN(2),
    //STOLEN(3),
    //ON_REPAIR(4),
    BORROWED(2),
    RETURNING(3);
    private final int code;

}
