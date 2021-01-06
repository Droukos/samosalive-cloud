package com.droukos.aedservice.environment.constants;

public class Fields {
    private Fields() {
    }

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String PASSWORD_CONFRIMED = "passwordConfirmed";

    public static final String USERINFO_PERSONAL = "prsn.";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String PROFILE = USERINFO_PERSONAL + "prof.";
    public static final String AVATAR = PROFILE + "av";
    public static final String DESCRIPTION = PROFILE + "desc";
    public static final String ADDRESS = USERINFO_PERSONAL + "addr.";
    public static final String COUNTRYISO = ADDRESS + "cIso";
    public static final String STATE = ADDRESS + "state";
    public static final String CITY = ADDRESS + "city";


    public static final String USERINFO_PRIVACY = "privy.";
    public static final String PRIVY_ONLINE_STATUS_TYPE = USERINFO_PRIVACY + "onST";
    public static final String PRIVY_ONLINE_STATUS = USERINFO_PRIVACY + "onStatus";
    public static final String PRIVY_LAST_LOGIN_TYPE = USERINFO_PRIVACY + "lstLoginT";
    public static final String PRIVY_LAST_LOGIN = USERINFO_PRIVACY + "lstLogin";
    public static final String PRIVY_LAST_LOGOUT_TYPE = USERINFO_PRIVACY + "lstLogoutT";
    public static final String PRIVY_LAST_LOGOUT = USERINFO_PRIVACY + "lstLogout";
    public static final String PRIVY_FULLNAME_TYPE = USERINFO_PRIVACY + "nameT";
    public static final String PRIVY_FULLNAME = USERINFO_PRIVACY + "name";
    public static final String PRIVY_EMAIL_TYPE = USERINFO_PRIVACY + "emailT";
    public static final String PRIVY_EMAIL = USERINFO_PRIVACY + EMAIL;
    public static final String PRIVY_ACCOUNT_CREATED_TYPE = USERINFO_PRIVACY + "accCT";
    public static final String PRIVY_ACCOUNT_CREATED = USERINFO_PRIVACY + "accC";
    public static final String PRIVY_DESCRIPTION_TYPE = USERINFO_PRIVACY + "descT";
    public static final String PRIVY_DESCRIPTION = USERINFO_PRIVACY + "desc";
    public static final String PRIVY_ADDRESS_TYPE = USERINFO_PRIVACY + "addrT";
    public static final String PRIVY_ADDRESS = USERINFO_PRIVACY + "addr";
    public static final String PRIVY_PHONE_TYPE = USERINFO_PERSONAL + "phoneT";
    public static final String PRIVY_PHONE = USERINFO_PERSONAL + "phone";

    public static final String USERINFO_SYSTEM = "sys.";
    public static final String SYS_CREDIBILITY_STARS = USERINFO_SYSTEM + "credStars";
    public static final String SYS_ACCOUNT_CREATED = USERINFO_SYSTEM + "accC";
    public static final String SYS_ACCOUNT_UPDATED = USERINFO_SYSTEM + "accU";
    public static final String SYS_LAST_LOGIN = USERINFO_SYSTEM + "lstLogin";
    public static final String SYS_LAST_LOGOUT = USERINFO_SYSTEM + "lstLogout";
    public static final String SYS_LAST_PASS_RESET = USERINFO_SYSTEM + "lstPassReset";
    public static final String SYS_VERIFICATION = USERINFO_SYSTEM + "verification";
    public static final String VERIFIED = SYS_VERIFICATION + "ver";
    public static final String VERIFIED_CODE = SYS_VERIFICATION + "verC";
    public static final String VERIFIED_ON = SYS_VERIFICATION + "verOn";
    public static final String SYS_IP_ADDRESS = USERINFO_SYSTEM + "ipAddr";


    public static final String USERINFO_STATE = "state.";
    public static final String STATE_ONLINE = USERINFO_STATE + "on";
    public static final String STATE_STATUS = USERINFO_STATE + "status";

    public static final String OCCURRENCE_TYPE = "occurrenceType";
    public static final String OCCURRENCE_COMMENT = "comment";

    public static final String CALLEE = "callee";
    public static final String PHONE = "phone";

    public static final String NEWS_TITLE = "newsTitle";
    public static final String NEWS_CONTENT = "content";
    public static final String NEWS = "news";

    public static final String PROBLEMS_TITLE = "title";
    public static final String PROBLEMS_INFO = "body";
}
