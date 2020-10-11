package com.droukos.userservice.environment.enums.tags;

public enum OccurrenceType {
    ARRHYTHMIAS(0,"events.eventS1"),
    BREATHING(1,"events.eventS2"),
    SENSES(2,"events.eventS3");

    private int code;
    private String translate;
    private int getCode()
    {
        return code;
    }
    private String getTranslate()
    {
        return translate;
    }
    OccurrenceType(int code, String translate)
    {
        this.code=code;
        this.translate=translate;
    }
}
