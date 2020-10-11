package com.droukos.userservice.environment.enums;

public enum Regexes {
    VALIDNAME("[a-zA-Z0-9_-]{2,20}"),
    PASSWORD("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}$"),
    PASSWORD_MEDIUM("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,160}$"),
    PASSWORD_WITH_SPECIAL_CHARS("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#!~;<>@#$%^&+=])(?=\\S+$).{6,}$"),
    EMAIL("^(.+)@(.+)$");


    private String regex;

    public String getRegex(){
        return regex;
    }

    Regexes(String regex){
        this.regex = regex;
    }
}
