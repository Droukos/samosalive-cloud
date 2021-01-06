package com.droukos.osmservice.environment.enums;

import com.droukos.osmservice.environment.constants.AedStatusCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AedEventStatus {
    PENDING(AedStatusCodes.PENDING),
    ONPROGRESS(AedStatusCodes.ONPROGRESS),
    COMPLETED(AedStatusCodes.COMPLETED);

    private final int status;
}
