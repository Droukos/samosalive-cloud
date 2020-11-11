package com.droukos.cdnservice.model.user.system.security;

import lombok.*;

import java.time.LocalDateTime;

@Data @ToString
@AllArgsConstructor
@Setter
@Getter
public class AccountLocked {
    private boolean accLock;
    private LocalDateTime until;
}
