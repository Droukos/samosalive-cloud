package com.droukos.userservice.environment.enums;

public enum ChatRole {

    ADMIN(0,"Admin"),
    USER(1, "User");

    private final String query;
    private final int code;

    public String query() {
        return query;
    }

    public int code() {
        return code;
    }

    ChatRole(int code, String query) {
        this.query = query;
        this.code = code;
    }
}
