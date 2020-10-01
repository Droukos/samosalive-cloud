package com.droukos.authservice.environment.roles.roles_list;

import com.droukos.authservice.environment.roles.roles_list.lvl1_roles.AdminRoles;
import com.droukos.authservice.environment.roles.roles_list.lvl1_roles.AedRoles;
import com.droukos.authservice.environment.roles.roles_list.lvl1_roles.UserRoles;
import com.droukos.authservice.environment.interfaces.core_roles.RoleInfo;
import com.droukos.authservice.environment.interfaces.core_roles.Roles0Info;

import static com.droukos.authservice.environment.services.GeneralSemantics.SPLITTER;

public enum LvL0Roles implements Roles0Info {
    ADMINS(0,"Admin" , AdminRoles.values()),
    AED(1000, "Aed", AedRoles.values()),
    USERS(10000, "User", UserRoles.values())
    ;

    private final String query;

    private final RoleInfo[] lvl1Roles;

    private final int code;

    public String query() {
        return query;
    }

    public int getCode() {
        return code;
    }

    @Override
    public RoleInfo[] getRolesInfo() {
        return lvl1Roles;
    }

    public String getCodeWithSplitter() {
        return code + SPLITTER.getCode();
    }
    public RoleInfo[] getLvl1Roles() {
        return lvl1Roles;
    }

    LvL0Roles(int code, String query, RoleInfo[] lvl1Roles) {
        this.query = query;
        this.code = code;
        this.lvl1Roles = lvl1Roles;
    }
}
