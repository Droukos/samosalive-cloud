package com.droukos.aedservice.model.user.system.security;

import com.droukos.aedservice.model.user.UserRes;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class AccountBanned {
    boolean accBanned;
    LocalDateTime until;

    public static AccountBanned noUpdate(UserRes user) {
        return new AccountBanned(
                user.getSys().getSec().getBan().isAccBanned(),
                user.getSys().getSec().getBan().getUntil()
        );
    }

    public static AccountBanned lockUser() {
        return new AccountBanned(
                true,
                LocalDateTime.now()
        );
    }

    public static AccountBanned unlockUser() {
        return new AccountBanned(
                false,
                LocalDateTime.now()
        );
    }
}
