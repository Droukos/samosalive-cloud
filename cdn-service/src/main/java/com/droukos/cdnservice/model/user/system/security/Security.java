package com.droukos.cdnservice.model.user.system.security;


import com.droukos.cdnservice.model.user.system.Verification;
import com.droukos.cdnservice.model.user.system.security.AccountBanned;
import com.droukos.cdnservice.model.user.system.security.AccountLocked;
import com.droukos.cdnservice.model.user.system.security.auth.PasswordReset;
import com.droukos.cdnservice.model.user.system.security.jwt.platforms.AndroidJWT;
import com.droukos.cdnservice.model.user.system.security.jwt.platforms.IosJWT;
import com.droukos.cdnservice.model.user.system.security.jwt.platforms.WebJWT;
import com.droukos.cdnservice.model.user.system.security.logins.AndroidLogins;
import com.droukos.cdnservice.model.user.system.security.logins.IosLogins;
import com.droukos.cdnservice.model.user.system.security.logins.WebLogins;
import lombok.*;

import java.util.List;

@Data @ToString
@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
public class Security {
    private AndroidLogins androidLog;
    private IosLogins iosLog;
    private WebLogins webLog;
    private List<PasswordReset> passResets;
    private AndroidJWT androidJWT;
    private IosJWT iosJWT;
    private WebJWT webJWT;
    private Verification verified;
    private AccountLocked lock;
    private AccountBanned ban;
}
