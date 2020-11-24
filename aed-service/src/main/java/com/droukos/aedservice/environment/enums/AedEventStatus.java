package com.droukos.aedservice.environment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AedEventStatus {
    PENDING(1),
    ONPROGRESS(2),
    COMPLETED(3);

    private final int status;
}
