package com.droukos.authservice.environment.services.lvl1_services;

import com.droukos.authservice.environment.interfaces.core_services.SecRunByInfo;
import com.droukos.authservice.environment.interfaces.core_services.ServiceInfo;
import com.droukos.authservice.environment.services.LvL0_Services;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.droukos.authservice.environment.services.sec_options.GeneralSecOptions.ONLY_GENERAL_ADMIN;

@RequiredArgsConstructor
public enum EnSysAdminServices implements ServiceInfo {
    GET_ROLES         ("get_roles"                  ,"0",   ONLY_GENERAL_ADMIN,       false,       false),

    ;

    @NonNull private final String url;
    @NonNull private final String serviceCode;
    @NonNull private final SecRunByInfo runByInfo;
    @NonNull private final boolean checkAccToken;
    @NonNull private final boolean runSecurity;

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
