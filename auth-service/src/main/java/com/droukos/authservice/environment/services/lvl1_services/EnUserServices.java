package com.droukos.authservice.environment.services.lvl1_services;

import com.droukos.authservice.environment.services.LvL0_Services;
import com.droukos.authservice.environment.interfaces.core_services.SecRunByInfo;
import com.droukos.authservice.environment.interfaces.core_services.ServiceInfo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.droukos.authservice.environment.services.sec_options.GeneralSecOptions.*;

@RequiredArgsConstructor
public enum EnUserServices implements ServiceInfo {
    FIND_USER_BY_ID                ("find_user/{id}"       ,"0",   ANY_USER,                false, true),
    FIND_USERNAME_LIKE             ("find_username"        ,"1",   ANY_USER,                false, true),
    FIND_USERS_PREVIEW             ("find_users_preview"   ,"2",   ANY_USER,                false, true),
    FIND_USER_DATA                 ("user_data"            ,"3",   ANY_USER,                false, true),
    DELETE_USER                    ("delete_user"          ,"4",   SAME_USER_OR_ANY_ADMIN,  true, true),
    PUT_USER_INFO_PERSONAL         ("personal/{id}"        ,"5",   SAME_USER_OR_ANY_ADMIN,  true, true),
    PUT_USER_INFO_AVATAR           ("avatar/{id}"          ,"6",   SAME_USER_OR_ANY_ADMIN,  true, true),
    PUT_USER_INFO_PRIVACY_SETS     ("privacy/{id}"         ,"7",   SAME_USER_OR_ANY_ADMIN,  true, true),
    PUT_USER_VERIFICATION          ("verification"         ,"8",   SAME_USER_OR_ANY_ADMIN,  true, true),
    PUT_STATE_AVAILABILITY         ("availability/{id}"    ,"9",   SAME_USER_OR_ANY_ADMIN,  true, true),
    ;

    @NonNull private final String url;
    @NonNull private final String serviceCode;
    @NonNull private final SecRunByInfo runByInfo;
    @NonNull private final boolean checkAccToken;
    @NonNull private final boolean runSecurity;

    public String getFullUrl() {
        return LvL0_Services.AUTH.getUrl() + url;
    }

    public String getUrl() {
        return url;
    }

    public String getServiceUrl() {
        if(url.contains("/")) return url.split("/")[0];
        else return url;
    }

    public String getServiceCode() {
        return LvL0_Services.USER.getServiceCode() + serviceCode;
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
