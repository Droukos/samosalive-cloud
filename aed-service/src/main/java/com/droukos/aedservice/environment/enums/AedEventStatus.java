package com.droukos.aedservice.environment.enums;

import com.droukos.aedservice.environment.constants.AedStatusCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AedEventStatus {
    PENDING(AedStatusCodes.PENDING),
    ONPROGRESS(AedStatusCodes.ONPROGRESS),
    COMPLETED(AedStatusCodes.COMPLETED),
    CLOSED(AedStatusCodes.CLOSED);

    private final int status;
}
