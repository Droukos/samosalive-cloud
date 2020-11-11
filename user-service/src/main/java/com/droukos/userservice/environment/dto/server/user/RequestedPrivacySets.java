package com.droukos.userservice.environment.dto.server.user;

import com.droukos.userservice.model.user.UserRes;
import com.droukos.userservice.model.user.privacy.PrivacySetting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import reactor.core.publisher.Mono;

import static com.droukos.userservice.environment.constants.FieldNames.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestedPrivacySets {
    private String userid;
    private String username;
    private PrivacySetting onlineStatus;
    private PrivacySetting lastLogin;
    private PrivacySetting lastLogout;
    private PrivacySetting fullname;
    private PrivacySetting email;
    private PrivacySetting accCreated;
    private PrivacySetting description;
    private PrivacySetting address;
    private PrivacySetting phones;

    public static Mono<RequestedPrivacySets> buildMono(UserRes user) {
        return Mono.just(build(user));
    }

    public static RequestedPrivacySets build(UserRes user) {
        return new RequestedPrivacySets(
                user.getId(),
                user.getUserC(),
                user.getPrivy().getPrivySets().get(F_PRIVY_ONLINE_STATUS),
                user.getPrivy().getPrivySets().get(F_PRIVY_LAST_LOGIN),
                user.getPrivy().getPrivySets().get(F_PRIVY_LAST_LOGOUT),
                user.getPrivy().getPrivySets().get(F_PRIVY_FULLNAME),
                user.getPrivy().getPrivySets().get(F_PRIVY_EMAIL),
                user.getPrivy().getPrivySets().get(F_PRIVY_ACCOUNT_CREATED),
                user.getPrivy().getPrivySets().get(F_PRIVY_DESCRIPTION),
                user.getPrivy().getPrivySets().get(F_PRIVY_ADDRESS),
                user.getPrivy().getPrivySets().get(F_PRIVY_PHONE)
        );
    }
}


