package com.droukos.authservice.environment.enums;

public enum  Availability {

    OFFLINE(0),
    ONLINE(1),
    INVISIBLE(2),
    BUSY(3),
    ON_DUTY(4),
    AWAY(5),
    TEMP_BANNED(6),
    LOCKED(7),
    PERM_BANNED(8);

    private final int code;

    public int getCode() {
        return code;
    }
    Availability(int code) {
        this.code = code;
    }
}
