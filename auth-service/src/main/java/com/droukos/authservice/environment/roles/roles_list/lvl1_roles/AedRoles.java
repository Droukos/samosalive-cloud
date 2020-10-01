package com.droukos.authservice.environment.roles.roles_list.lvl1_roles;

import com.droukos.authservice.environment.interfaces.core_roles.RoleInfo;

public enum  AedRoles implements RoleInfo {
    RESCUER("100", "Rescuer"),

    DEFIBRILLATOR_MANAGER("140", ""),
    DEFIBRILLATOR_COMPANY_SERVICE("200", ""),

    EKAB("500", "")
    ;

    private final String query;

    private final String code;

    AedRoles(String code, String query) {
        this.code = code;
        this.query = query;
    }

    @Override
    public String getRoleCode() {
        return null;
    }

    @Override
    public String getRoleQuery() {
        return null;
    }
}
