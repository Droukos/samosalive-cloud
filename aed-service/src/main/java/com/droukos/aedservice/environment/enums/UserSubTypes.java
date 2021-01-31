package com.droukos.aedservice.environment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserSubTypes {
    AED_EVENT(10);

    private final int code;
}
