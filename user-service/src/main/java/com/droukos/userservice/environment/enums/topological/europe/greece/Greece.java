package com.droukos.userservice.environment.enums.topological.europe.greece;

import static com.droukos.userservice.environment.enums.topological.europe.Europe.GREECE;

public enum  Greece {

    ATTICA(GREECE.getCode()+"1.", GREECE.query()+"Attica "),
    ;
    private final String code;
    private final String query;

    public String query() {
        return query;
    }

    public String getCode() {
        return code;
    }

    Greece(String code, String query) {
        this.query = query;
        this.code = code;
    }
}
