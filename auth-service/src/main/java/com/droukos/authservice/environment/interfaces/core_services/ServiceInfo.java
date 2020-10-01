package com.droukos.authservice.environment.interfaces.core_services;

public interface ServiceInfo {
    String getServiceUrl();
    String getServiceCode();
    SecRunByInfo getRunByInfo();
    boolean chkAccToken();
    boolean runSecurity();
}
