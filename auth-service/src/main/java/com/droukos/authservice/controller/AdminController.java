package com.droukos.authservice.controller;

import com.droukos.authservice.environment.dto.client.admin.*;
import com.droukos.authservice.environment.dto.server.auth.login.LoginResponse;
import com.droukos.authservice.model.factories.user.security.status.UserBanFactory;
import com.droukos.authservice.service.admin.AdminChannelService;
import com.droukos.authservice.service.auth.AuthServices;
import com.droukos.authservice.service.auth.BanServices;
import com.droukos.authservice.service.auth.RolesService;
import com.droukos.authservice.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.droukos.authservice.util.factories.HttpExceptionFactory.badRequest;

@Controller
@AllArgsConstructor
public class AdminController {

    private final AdminChannelService adminChannelService;
    private final AuthServices authServices;
    private final BanServices banServices;
    private final RolesService rolesService;

    @MessageMapping("auth.admin.ban.users")
    public Mono<Boolean> permBanUsers(BanUsers banUsers) {
        Map<String, Long> banUserMap = banUsers.getBanUsers()
                .stream()
                .collect(Collectors.toMap(BanUser::getUsername, BanUser::getDuration));

        return authServices.getUsersByUsername(new ArrayList<>(banUserMap.keySet()))
                .flatMap(user->
                    banUserMap.get(user.getUser()) == 0
                        ? UserBanFactory.permBanUserMono(user)
                        : UserBanFactory.tempBanUserMono(user, banUserMap.get(user.getUser())))
                .collectList()
                .flatMapMany(banServices::saveUsers)
                .flatMap(adminChannelService::publishUserAuthOnRedis)
                .then(Mono.just(true));
    }

    @MessageMapping("auth.admin.unban.users")
    public Mono<Boolean> unbanUsers(BannedUsers bannedUsers) {

        return authServices.getUsersByUsername(bannedUsers.getBannedUsers()
                .stream()
                .map(BannedUser::getUsername)
                .collect(Collectors.toList()))
                .flatMap(UserBanFactory::unbanUserMono)
                .collectList()
                .flatMapMany(banServices::saveUsers)
                .then(Mono.just(true));
    }

    @MessageMapping("auth.admin.users.role.change")
    public Mono<Boolean> usersRoleChange(ChangeRoles changeRoles) {

        if(changeRoles.getChangeRoles().size() > 20) return Mono.error(badRequest());
        Map<String, ChangeRole> changeRoleMap = changeRoles.getChangeRoles()
                .stream()
                .collect(Collectors.toMap(ChangeRole::getUsername, Function.identity()));

        return rolesService.validateChangeRoles(changeRoles)
                .then(Mono.just(new ArrayList<>(changeRoleMap.keySet())))
                .flatMapMany(authServices::getUsersByUsername)
                .zipWith(Mono.just(changeRoleMap))
                .flatMap(rolesService::validateUserOldRole)
                .flatMap(rolesService::updateUserWithNewRole)
                .collectList()
                .flatMapMany(rolesService::saveAllUsersToMongoDb)
                .flatMap(adminChannelService::publishUserAuthOnRedis)
                .then(Mono.just(true));
    }

    @MessageMapping("auth.admin.users.role.add")
    public Mono<Boolean> usersRoleAdd(UpRoles upRoles) {

        Mono<Map<String, String>> userRolesMapMono = rolesService.createUserRoleMap(upRoles);

        return rolesService.validateRolesAndFetchUsers(userRolesMapMono)
                .flatMap(rolesService::validateUserCanAddRole)
                .flatMap(rolesService::updateUserWithAddedRole)
                .collectList()
                .flatMapMany(rolesService::saveAllUsersToMongoDb)
                .flatMap(adminChannelService::publishUserAuthOnRedis)
                .then(Mono.just(true));
    }

    @MessageMapping("auth.admin.users.role.del")
    public Mono<Boolean> usersRoleDel(UpRoles upRoles) {

        Mono<Map<String, String>> userRolesMapMono = rolesService.createUserRoleMap(upRoles);

        return rolesService.validateRolesAndFetchUsers(userRolesMapMono)
                .flatMap(rolesService::validateUserCanDelRole)
                .flatMap(rolesService::updateUserWithDelRole)
                .collectList()
                .flatMapMany(rolesService::saveAllUsersToMongoDb)
                .flatMap(adminChannelService::publishUserAuthOnRedis)
                .then(Mono.just(true));
    }

    @MessageMapping("auth.listen")
    public Flux<LoginResponse> listenToAuth() {

        return ReactiveSecurityContextHolder.getContext()
                .flatMap(SecurityUtil::getRequesterDataMono)
                .flatMapMany(adminChannelService::listenToAuth);
    }

    //@MessageMapping("auth.admin.temp.ban.user")
    //public Mono<Boolean> tempBanUser(BanUser banUser) {
    //    return authServices.getUserByUsername(banUser.getUserByUsername())
    //            .zipWith(Mono.just(banUser))
    //            .flatMap(UserBanFactory::tempBanUserMono)
    //            .flatMap(banServices::saveUser)
    //            .flatMap(banServices::removeAllAccessTokens)
    //            .then(Mono.just(true));
    //}

    //@MessageMapping("auth.admin.temp.ban.users")
    //public Mono<Boolean> tempBanUsers(BanUsers banUsers) {
    //    return authServices.getUsersByUsername(banUsers.)
    //}

    //@MessageMapping("auth.admin.perm.ban.user")
    //public Mono<Boolean> permBanUser(BanUser user) {
    //    return authServices.getUserByUsername(user.getUsername())
    //            .flatMap(UserBanFactory::permBanUserMono)
    //            .flatMap(banServices::saveUser)
    //            .flatMap(banServices::removeAllAccessTokens)
    //            .then(Mono.just(true));
    //}

    //@MessageMapping("auth.admin.unban.user")
    //public Mono<Boolean> unbanUser(BannedUser bannedUser) {
    //    return authServices.getUserById(bannedUser.getUserid())
    //            .flatMap(UserBanFactory::unbanUserMono)
    //            .flatMap(banServices::saveUser)
    //            .then(Mono.just(true));
    //}

    //@MessageMapping("auth.admin.user.role.change")
    //public Mono<Boolean> userRoleChange() {
//
    //
    //}

    //@MessageMapping("auth.admin.user.role.add")
    //public Mono<Boolean> userRoleAdd() {
//
    //}

    //@MessageMapping("auth.admin.user.role.del")
    //public Mono<Boolean> userRoleDel() {
//
    //}
}
