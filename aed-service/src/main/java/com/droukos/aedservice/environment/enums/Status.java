package com.droukos.aedservice.environment.enums;

public enum Status {

    OFFLINE(0),
    ONLINE(1),
    BUSY(2),
    ON_DUTY(3),
    AWAY(4);

    private final int code;

    public int getCode() {
        return code;
    }

    Status(int code) {
        this.code = code;
    }

}
