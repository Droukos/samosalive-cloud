package com.droukos.osmservice.environment.enums;

import com.droukos.osmservice.environment.constants.PrivacyCodes;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PrivacySettings {
    PUBLIC  (PrivacyCodes.PUBLIC,   "PUBLIC"),
    ONLY_TO (PrivacyCodes.ONLY_TO,  "ONLY_TO"),
    NOT_TO  (PrivacyCodes.NOT_TO,   "NOT_TO"),
    PRIVATE (PrivacyCodes.PRIVATE,  "PRIVATE");

    private final int code;
    private final String query;

    public String query() {
        return query;
    }

    public int code() {
        return code;
    }
}
