package com.droukos.aedservice.environment.enums;

public enum AccountStatus {
    ACTIVE(0),
    LOCKED(1),
    DEACTIVATED(2),

    ;

    private final int code;

    public int getCode() {
        return code;
    }

    AccountStatus(int code) {
        this.code = code;
    }
}