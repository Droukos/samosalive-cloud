package com.droukos.newsservice.environment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TagList {
    IDEAS(0),
    DEFIBRILLATOR(1),
    HEALTH(2),
    SAMOS(3),
    OTHER(4);

    private final int tag;
}
