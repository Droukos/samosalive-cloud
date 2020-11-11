package com.droukos.userservice.model.user.system.security;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AccountBanned {
    private boolean accBanned;
    private LocalDateTime until;
}
