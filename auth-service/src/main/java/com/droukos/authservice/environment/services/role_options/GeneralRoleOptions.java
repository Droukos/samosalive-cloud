package com.droukos.authservice.environment.services.role_options;

import com.droukos.authservice.environment.interfaces.core_services.RoleOptionsInfo;

import static com.droukos.authservice.environment.services.GeneralSemantics.ANY;
import static com.droukos.authservice.environment.services.GeneralSemantics.NONE;

public enum GeneralRoleOptions implements RoleOptionsInfo {
    EVERYONE    (ANY.getCode()  +"."+   ANY.getCode()),
    NO_ONE      (NONE.getCode() +"."+   NONE.getCode()),
    NO_GUEST    (";.;");

    private final String code;

    public String getCode() {
        return code;
    }

    GeneralRoleOptions(String code) {
        this.code = code;
    }
}
