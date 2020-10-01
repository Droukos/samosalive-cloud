package com.droukos.authservice.environment.services;

public enum GeneralSemantics {
    SPLITTER("."),

    BETWEEN("_"),
    AND_ABOVE("&+"),
    AND_BELOW("&-"),
    OR_ABOVE("|+"),
    OR_BELOW("|-"),


    ANY("*"),
    NONE(";")
    ;

    private final String code;

    public String getCode() {
        return code;
    }

    public String getEscapedCode() {
        return "\\"+code;
    }

    GeneralSemantics(String code) {
        this.code = code;
    }
}
