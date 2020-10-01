package com.droukos.authservice.environment.roles.roles_list.lvl1_roles;

import com.droukos.authservice.environment.constants.authorities.Roles;
import com.droukos.authservice.environment.interfaces.core_roles.RoleInfo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.droukos.authservice.environment.roles.roles_list.LvL0Roles.ADMINS;

@RequiredArgsConstructor
public enum AdminRoles implements RoleInfo {
    GENERAL_ADMIN       (0, "General_", Roles.GENERAL_ADMIN),
    AREA_ADMIN          (1000,"Area_", Roles.AREA_ADMIN),
    ;

    @NonNull private final int code;
    @NonNull private final String query;
    @NonNull private final String roleAuth;

    public String query() {
        return query + ADMINS.query();
    }

    public String authQuery() {
        return ("ROLE_" + roleAuth);
    }

    public String getCode() {
        return ADMINS.getCodeWithSplitter() + code;
    }

    @Override
    public String getRoleCode() {
        return this.getCode();
    }

    @Override
    public String getRoleQuery() {
        return this.query();
    }
}
