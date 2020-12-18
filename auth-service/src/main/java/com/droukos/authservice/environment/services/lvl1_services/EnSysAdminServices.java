package com.droukos.authservice.environment.services.lvl1_services;

import com.droukos.authservice.environment.interfaces.core_services.SecRunByInfo;
import com.droukos.authservice.environment.interfaces.core_services.ServiceInfo;
import com.droukos.authservice.environment.services.LvL0_Services;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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

    public String getServiceCode() {
        return LvL0_Services.AUTH.getServiceCode() + serviceCode;
    }

    public String getFullUrl() {
        return "/"+ LvL0_Services.AUTH.getUrl() +"/"+ url;
    }

    public String getUrl() {
        return url;
    }

    public String getServiceUrl() {
        if(url.contains("/")) return url.split("/")[0];
        else return url;
    }

    public SecRunByInfo getRunByInfo() {
        return runByInfo;
    }

    @Override
    public boolean chkAccToken() {
        return checkAccToken;
    }

    public boolean runSecurity() {
        return runSecurity;
    }
}
