package com.droukos.userservice.environment.interfaces.core_services;

import java.util.List;

public interface SecRunByInfo {
    List<String> getCodes();
    boolean getPutFromSameUserId();
}
