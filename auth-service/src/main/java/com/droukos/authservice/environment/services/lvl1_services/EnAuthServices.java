package com.droukos.authservice.environment.services.lvl1_services;

import com.droukos.authservice.environment.interfaces.core_services.SecRunByInfo;
import com.droukos.authservice.environment.interfaces.core_services.ServiceInfo;
import com.droukos.authservice.environment.services.LvL0_Services;
import lombok.AllArgsConstructor;

import static com.droukos.authservice.environment.services.sec_options.GeneralSecOptions.*;

/**
 * @author Kostas
 */

@AllArgsConstructor
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

    @Override
    public String getServiceUrl() {
        return url.contains("/{id}")? url.replace("/{id}", ""): url;
    }

    @Override
    public SecRunByInfo getRunByInfo() {
        return runByInfo;
    }

    @Override
    public boolean runSecurity() {
        return runSecurity;
    }

}
