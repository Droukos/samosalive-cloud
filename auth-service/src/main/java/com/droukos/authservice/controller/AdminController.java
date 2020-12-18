package com.droukos.authservice.controller;

import com.droukos.authservice.environment.dto.client.auth.BanUser;
import com.droukos.authservice.environment.dto.client.auth.BannedUser;
import com.droukos.authservice.model.factories.user.security.status.UserBanFactory;
import com.droukos.authservice.service.auth.AuthServices;
import com.droukos.authservice.service.auth.BanServices;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class AdminController {

    private final AuthServices authServices;
    private final BanServices banServices;

    @MessageMapping("auth.admin.perm.ban.user")
    public Mono<Boolean> permBanUser(BanUser user) {
        return authServices.getUserById(user.getUserid())
                .flatMap(UserBanFactory::permBanUserMono)
                .flatMap(banServices::saveUser)
                .flatMap(banServices::removeAllAccessTokens)
                .then(Mono.just(true));
    }

    @MessageMapping("auth.admin.temp.ban.user")
    public Mono<Boolean> tempBanUser(BanUser banUser) {
        return authServices.getUserById(banUser.getUserid())
                .zipWith(Mono.just(banUser))
                .flatMap(UserBanFactory::tempBanUserMono)
                .flatMap(banServices::saveUser)
                .flatMap(banServices::removeAllAccessTokens)
                .then(Mono.just(true));
    }

    @MessageMapping("auth.admin.unban.user")
    public Mono<Boolean> unbanUser(BannedUser bannedUser) {
        return authServices.getUserById(bannedUser.getUserid())
                .flatMap(UserBanFactory::unbanUserMono)
                .flatMap(banServices::saveUser)
                .then(Mono.just(true));
    }
}
