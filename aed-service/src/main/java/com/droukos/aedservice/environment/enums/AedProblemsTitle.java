package com.droukos.aedservice.environment.enums;

import com.droukos.aedservice.environment.constants.AedProblemsCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AedProblemsTitle {
    BATTERY(AedProblemsCodes.BATTERY),
    BROKEN(AedProblemsCodes.BROKEN),
    MISSING(AedProblemsCodes.MISSING),
    OTHER(AedProblemsCodes.OTHER);

    private final int title;
}
