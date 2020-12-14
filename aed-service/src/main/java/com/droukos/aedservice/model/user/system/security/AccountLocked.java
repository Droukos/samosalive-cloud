package com.droukos.aedservice.model.user.system.security;

import com.droukos.aedservice.model.user.UserRes;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class AccountLocked {
    boolean accLock;
    LocalDateTime until;

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
