package com.droukos.authservice.environment.services.lvl1_services;

import com.droukos.authservice.environment.services.LvL0_Services;
import com.droukos.authservice.environment.interfaces.core_services.SecRunByInfo;
import com.droukos.authservice.environment.interfaces.core_services.ServiceInfo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.droukos.authservice.environment.services.sec_options.GeneralSecOptions.*;

@RequiredArgsConstructor
public enum EnAuthServices implements ServiceInfo {
    SIGN_UP         ("sign_up"                  ,"0",   NO_RUN_BY_SEC,             false   ,false),
    LOGIN           ("login"                    ,"1",   NO_RUN_BY_SEC,             false  ,false),
    CHECK_USERNAME  ("check_username"           ,"2",   NO_RUN_BY_SEC,             false   ,false),
    CHECK_EMAIL     ("check_email"              ,"3",   NO_RUN_BY_SEC,             false   ,false),
    PASSWORD_RESET  ("password_reset/{id}"      ,"4",   SAME_USER_OR_ANY_ADMIN,    true   ,true),
    PASSWORD_CHANGE ("password_change/{id}"     ,"5",   SAME_USER_OR_ANY_ADMIN,    true   ,true),
    PUT_ROLE_ADD    ("role_add/{id}"            ,"6",   ONLY_GENERAL_ADMIN,        true   ,true),
    PUT_ROLE_DEL    ("role_del/{id}"            ,"7",   ONLY_GENERAL_ADMIN,        true   ,true),
    EMAIL_CHANGE    ("email_change/{id}"        ,"8",   SAME_USER_OR_ANY_ADMIN,    true   ,true),
    REVOKE_TOKENS   ("revoke_tokens/{id}"       ,"9",   SAME_USER_OR_ANY_ADMIN,    true   ,true),
    LOGOUT          ("logout"                   ,"10",  NO_RUN_BY_SEC,            true   ,true),
    USER_DATA       ("token/user_data"          ,"11",  NO_RUN_BY_SEC,             false   ,false),
    ACCESS_TOKEN    ("token/access_token"       ,"12",  NO_RUN_BY_SEC,             false   ,false)
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

    @Override
    public boolean chkAccToken() {
        return checkAccToken;
    }

    public String getUrl() {
        return url;
    }

    public boolean getCheckAccToken() {
        return checkAccToken;
    }

    public String getServiceUrl() {
        if(url.contains("/{id}")) return url.replace("/{id}", "");
        else return url;
    }

    public SecRunByInfo getRunByInfo() {
        return runByInfo;
    }

    public boolean runSecurity() {
        return runSecurity;
    }

}
