package com.droukos.userservice.environment.enums.topological;

public enum Topological {
    //Planets-Earth
    EARTH("0.", "Earth "),
    //Continents-Earth
    EUROPE(EARTH.getCode()+"1.",EARTH.query()+"Europe "),
    ASIA(EARTH.getCode()+"2.", EARTH.query()+"Asia "),
    AFRICA(EARTH.getCode()+"3.", EARTH.query()+"Africa "),
    ;
    private final String code;
    private final String query;

    public String query() {
        return query;
    }

    public String getCode() {
        return code;
    }

    Topological(String code, String query) {
        this.query = query;
        this.code = code;
    }
}
