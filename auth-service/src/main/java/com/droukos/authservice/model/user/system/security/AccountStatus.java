package com.droukos.authservice.model.user.system.security;

import lombok.*;

import java.time.LocalDateTime;

import static com.droukos.authservice.environment.enums.AccountStatus.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AccountStatus {
    private int stat;
    private LocalDateTime until;

    public static AccountStatus permBanUser() {
        return new AccountStatus(
               PERM_BANNED.getCode(),
                null
        );
    }

    public static AccountStatus tempBanUser(long untilHours) {
        return new AccountStatus(
                TEMP_BANNED.getCode(),
                LocalDateTime.now().plusHours(untilHours)
        );
    }

    public static AccountStatus unbanUser() {
        return new AccountStatus(
                ACTIVE.getCode(),
               null
        );
    }
}
