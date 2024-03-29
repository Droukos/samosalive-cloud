package com.droukos.authservice.environment.enums;

public enum AccountStatus {
    ACTIVE(0),
    LOCKED(1),
    DEACTIVATED(2),
    TEMP_BANNED(4),
    PERM_BANNED(5),
    ;

    private final int code;

    public int getCode() {
        return code;
    }

    AccountStatus(int code) {
        this.code = code;
    }
}