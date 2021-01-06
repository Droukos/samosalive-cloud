package com.droukos.authservice.model.user.system.security;

import com.droukos.authservice.model.user.UserRes;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AccountLocked {
    private boolean accLock;
    private LocalDateTime until;

    public static AccountLocked noUpdate(UserRes user) {
        return new AccountLocked(
                user.getSys().getSec().getLock().isAccLock(),
                user.getSys().getSec().getLock().getUntil()
        );
    }

    public static AccountLocked lockUser() {
        return new AccountLocked(
                true,
                LocalDateTime.now()
        );
    }

    public static AccountLocked unlockUser() {
        return new AccountLocked(
                false,
                LocalDateTime.now()
        );
    }
}
