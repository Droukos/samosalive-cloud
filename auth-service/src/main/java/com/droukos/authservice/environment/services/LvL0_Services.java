package com.droukos.authservice.environment.services;

import com.droukos.authservice.environment.interfaces.core_services.Service0Info;
import com.droukos.authservice.environment.interfaces.core_services.ServiceInfo;
import com.droukos.authservice.environment.services.lvl1_services.EnAuthServices;
import com.droukos.authservice.environment.services.lvl1_services.EnSysAdminServices;
import com.droukos.authservice.environment.services.lvl1_services.EnUserServices;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/*
* Level 0 Services-> Handlers have a unique code
*/
@RequiredArgsConstructor
public enum LvL0_Services implements Service0Info {
    SYS_ADMIN("api/sys_admin", "0", EnSysAdminServices.values()),
    AUTH("api/auth","1", EnAuthServices.values()),
    USER("api/user","2", EnUserServices.values()),
    //CHAT("api/chat","2", null),
    ;

    @NonNull private final String url;
    @NonNull private final String serviceCode;
    @NonNull private final ServiceInfo[] lvl1Services;

    public String getServiceCode() {
        return serviceCode + GeneralSemantics.SPLITTER.getCode();
    }

    public String getUrl() {
        return this.url;
    }

    public ServiceInfo[] getLvl1Services() {
        return lvl1Services;
    }

    @Override
    public String getServiceUrl() {
        return url;
    }

    @Override
    public ServiceInfo[] getInfoServices() {
        return lvl1Services;
    }
}
