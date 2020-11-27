package com.droukos.userservice.model.user.system.security;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AccountLocked {
    private boolean accLock;
    private LocalDateTime until;
}
