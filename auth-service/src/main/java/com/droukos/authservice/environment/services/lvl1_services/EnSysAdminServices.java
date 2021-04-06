package com.droukos.authservice.environment.services.lvl1_services;

import com.droukos.authservice.environment.interfaces.core_services.SecRunByInfo;
import com.droukos.authservice.environment.interfaces.core_services.ServiceInfo;
import com.droukos.authservice.environment.services.LvL0_Services;
import lombok.AllArgsConstructor;

import static com.droukos.authservice.environment.services.sec_options.GeneralSecOptions.ONLY_GENERAL_ADMIN;

@AllArgsConstructor
public enum EnSysAdminServices implements ServiceInfo {
    GET_ROLES         ("get_roles"                  ,"0",   ONLY_GENERAL_ADMIN,       false,       false),
    ;

    private final String url;
    private final String serviceCode;
    private final SecRunByInfo runByInfo;
    private final boolean checkAccToken;
    private final boolean runSecurity;

    @Override
    public String getServiceCode() {
        return LvL0_Services.AUTH.getServiceCode() + serviceCode;
    }

    public String getFullUrl() {
        return "/"+ LvL0_Services.AUTH.getUrl() +"/"+ url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String getServiceUrl() {
        return url.contains("/")? url.split("/")[0] : url;
    }

    @Override
    public SecRunByInfo getRunByInfo() {
        return runByInfo;
    }

    @Override
    public boolean chkAccToken() {
        return checkAccToken;
    }

    @Override
    public boolean runSecurity() {
        return runSecurity;
    }
}
