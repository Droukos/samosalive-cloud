package com.droukos.userservice.environment.enums;

public enum Visibility {
    VISIBLE("visible"),
    VISIBLE_ONLY_TO("visible_only_to"),
    NOT_VISIBLE_TO("not_visible_to"),
    INVISIBLE("invisible");

    private String query;

    public String query() {
        return query;
    }

    Visibility (String query) {
        this.query = query;
    }
}
