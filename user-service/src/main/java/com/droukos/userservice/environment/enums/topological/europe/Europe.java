package com.droukos.userservice.environment.enums.topological.europe;


import static com.droukos.userservice.environment.enums.topological.Topological.EUROPE;

public enum  Europe {
    GREECE(EUROPE.getCode()+"1.", EUROPE.query()+"Greece ")
    ;
    private final String code;
    private final String query;

    public String query() {
        return query;
    }

    public String getCode() {
        return code;
    }

    Europe(String code, String query) {
        this.query = query;
        this.code = code;
    }
}
