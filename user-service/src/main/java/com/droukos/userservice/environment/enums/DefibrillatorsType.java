package com.droukos.userservice.environment.enums;

public enum DefibrillatorsType {

    MAN_EXT_DEF(0, "manual_external_defibrillator", "Manual External Defibrillator"),
    IMP_CARD_DEF(1, "implantable_cardioverter_defibrillator", "Implantable Cardioverter Defibrillator"),
    MAN_INT_DEF(2, "manual_internal_defibrillator", "Manual Internal Defibrillator"),
    AUTO_EXT_DEF(3,"automated_external_defibrillator", "Automated External Defibrillator"),
    WEAR_CARD_DEF(4, "wearable_cardiac_defibrillator", "Wearable Cardiac Defibrillator");

    private final String typeCanon;
    private final String type;

    private final int code;

    public String queryCanon() {
        return typeCanon;
    }

    public String query() {
        return type;
    }

    public int getCode() {
        return code;
    }

    DefibrillatorsType(int code, String typeCanon, String type) {
        this.typeCanon = typeCanon;
        this.type = type;
        this.code = code;
    }

}
