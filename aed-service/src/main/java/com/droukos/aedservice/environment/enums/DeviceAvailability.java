package com.droukos.aedservice.environment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeviceAvailability {
    AVAILABLE(0),
    UNAVAILABLE(1);
    private final int code;

}
