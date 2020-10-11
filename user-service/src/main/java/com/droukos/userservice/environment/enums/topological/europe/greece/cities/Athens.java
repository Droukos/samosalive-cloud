package com.droukos.userservice.environment.enums.topological.europe.greece.cities;

import static com.droukos.userservice.environment.enums.topological.europe.greece.provinces.Attica.ATHENS;

public enum Athens {
    ZOGRAFOU(ATHENS.getCode()+"1.", ATHENS.query()+"Zografou "),

    ;
    private final String code;
    private final String query;

    public String query() {
        return query;
    }

    public String getCode() {
        return code;
    }

    Athens(String code, String query) {
        this.query = query;
        this.code = code;
    }
}
