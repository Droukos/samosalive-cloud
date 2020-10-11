package com.droukos.userservice.environment.enums.topological.europe.greece.provinces;

import static com.droukos.userservice.environment.enums.topological.europe.greece.Greece.ATTICA;

public enum Attica {

    ATHENS(ATTICA.getCode()+"1.", ATTICA.query()+"Attica "),
    ;
    private final String code;
    private final String query;

    public String query() {
        return query;
    }

    public String getCode() {
        return code;
    }

    Attica(String code, String query) {
        this.query = query;
        this.code = code;
    }
}
