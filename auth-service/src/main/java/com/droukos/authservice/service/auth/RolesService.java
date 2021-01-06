package com.droukos.authservice.service.auth;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.RequesterAccessTokenData;
import com.droukos.authservice.environment.dto.client.admin.ChangeRole;
import com.droukos.authservice.environment.dto.client.admin.ChangeRoles;
import com.droukos.authservice.environment.dto.client.admin.UpRole;
import com.droukos.authservice.environment.dto.client.admin.UpRoles;
import com.droukos.authservice.environment.dto.client.auth.UpdateRole;
import com.droukos.authservice.environment.dto.server.auth.token.NewAccessTokenResponse;
import com.droukos.authservice.environment.security.TokenService;
import com.droukos.authservice.model.factories.user.res.role.UserFactoryAddDelRole;
import com.droukos.authservice.model.factories.user.res.role_tokens.UserFactoryAllTokenRoles;
import com.droukos.authservice.model.factories.user.res.role_tokens.UserFactoryAndroidTokenRole;
import com.droukos.authservice.model.factories.user.res.role_tokens.UserFactoryIosTokenRole;
import com.droukos.authservice.model.factories.user.res.role_tokens.UserFactoryWebTokenRole;
import com.droukos.authservice.model.user.RoleModel;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.repo.UserRepository;
import com.droukos.authservice.util.RolesUtil;
import com.droukos.authservice.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.droukos.authservice.environment.constants.Platforms.*;
import static com.droukos.authservice.model.factories.user.res.role.UserFactoryChangeRole.changeUserRoleMono;
import static com.droukos.authservice.service.validator.auth.ValidatorFactory.validateAddRole;
import static com.droukos.authservice.service.validator.auth.ValidatorFactory.validateDelRole;
import static com.droukos.authservice.util.RolesUtil.doesRoleNotExist;
import static com.droukos.authservice.util.factories.HttpExceptionFactory.badRequest;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@AllArgsConstructor
public class RolesService {

    private final AuthServices authServices;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public void validateRoleAddUpdate(Tuple2<UpdateRole, UserRes> tuple2) {
        validateAddRole(new UpdateRole(tuple2.getT1().getUpdatedRole(),
                tuple2.getT2().getAllRoles().stream().map(RoleModel::getRole).collect(Collectors.toList())));
    }

    public void validateRoleDelUpdate(Tuple2<UpdateRole, UserRes> tuple2) {
        validateDelRole(new UpdateRole(tuple2.getT1().getUpdatedRole(),
                tuple2.getT2().getAllRoles().stream().map(RoleModel::getRole).collect(Collectors.toList())));
    }

    public Mono<Tuple2<UserRes, NewAccTokenData>> itsSameZipAddedUserRoleAndTokens
            (Tuple4<UpdateRole, UserRes, SecurityContext, NewAccTokenData> tuple4) {


        RequesterAccessTokenData requesterData = SecurityUtil.getRequesterData(tuple4.getT3());
        UserRes user = tuple4.getT2();
        NewAccTokenData tokenData = tuple4.getT4();
        String newRole = tuple4.getT1().getUpdatedRole();
        String addedBy = requesterData.getUserId();

        return Mono.zip(
                Mono.just(switch (requesterData.getUserDevice()) {
                    case IOS -> UserFactoryIosTokenRole.addRoleIosAccessTokens(user, tokenData, newRole, addedBy);
                    case WEB -> UserFactoryWebTokenRole.addRoleWebAccessTokens(user, tokenData, newRole, addedBy);
                    default -> UserFactoryAndroidTokenRole.addRoleAndroidAccessTokens(user, tokenData, newRole, addedBy);
                }),
                Mono.just(tuple4.getT4()));
    }

    public Mono<Tuple2<UserRes, NewAccTokenData>> itsSameZipRemovedUserRoleAndTokens
            (Tuple4<UpdateRole, UserRes, SecurityContext, NewAccTokenData> tuple4) {


        UserRes user = tuple4.getT2();
        NewAccTokenData tokenData = tuple4.getT4();
        String newRole = tuple4.getT1().getUpdatedRole();

        return Mono.zip(
                Mono.just(
                        switch (SecurityUtil.getRequesterDevice(tuple4.getT3())) {
                            case IOS -> UserFactoryIosTokenRole.removedRoleIosAccessTokens(user, tokenData, newRole);
                            case WEB -> UserFactoryWebTokenRole.removedRoleWebAccessTokens(user, tokenData, newRole);
                            default -> UserFactoryAndroidTokenRole.removedRoleAndroidAccessTokens(user, tokenData, newRole);
                        }),
                Mono.just(tuple4.getT4()));
    }

    public Mono<Tuple2<UserRes, NewAccTokenData>> notSameZipAddedUserRoleAndTokens
            (Tuple4<UpdateRole, UserRes, SecurityContext, NewAccTokenData> tuple4) {


        UserRes user = tuple4.getT2();
        String newRole = tuple4.getT1().getUpdatedRole();
        String addedBy = SecurityUtil.getRequesterUserId(tuple4.getT3());

        return Mono.zip(
                Mono.just(UserFactoryAllTokenRoles.addRoleDeleteAllAccessTokens(user, newRole, addedBy)),
                Mono.just(NewAccTokenData.nullAccessToken()));
    }

    public Mono<Tuple2<UserRes, NewAccTokenData>> notSameZipRemovedUserRoleAndTokens
            (Tuple4<UpdateRole, UserRes, SecurityContext, NewAccTokenData> tuple4) {

        UserRes user = tuple4.getT2();
        String oldRole = tuple4.getT1().getUpdatedRole();

        return Mono.zip(
                Mono.just(UserFactoryAllTokenRoles.removedRoleDeleteAllAccessTokens(user, oldRole)),
                Mono.just(NewAccTokenData.nullAccessToken()));
    }

    public Mono<Tuple2<UserRes, NewAccTokenData>> saveUserToMongoDb(Tuple2<UserRes, NewAccTokenData> tuple2) {
        return userRepository.save(tuple2.getT1())
                .then(Mono.zip(
                        Mono.just(tuple2.getT1()),
                        Mono.just(tuple2.getT2()))
                );
    }

    public Mono<NewAccTokenData> saveNewAccessTokenIdToRedis(Tuple2<UserRes, NewAccTokenData> tuple2) {
        return tokenService.redisSetUserToken(tuple2.getT1(), tuple2.getT2())
                .then(Mono.just(tuple2.getT2()));
    }

    public Mono<NewAccTokenData> removeAllAccessTokenIdFromRedis(Tuple2<UserRes, NewAccTokenData> tuple2) {
        return tokenService.redisRemoveAllUserAccessTokens(tuple2.getT1())
                .then(Mono.just(tuple2.getT2()));
    }

    public Mono<ServerResponse> newAccessTokenResponse(NewAccTokenData accessTokenData) {
        return ok().body(fromValue(new NewAccessTokenResponse(accessTokenData.getToken())));
    }

    public Mono<ServerResponse> justMessageResponse() {
        return ok().body(fromValue("user.role.updated"));
    }

    public Mono<Map<String, String>> createUserRoleMap(UpRoles upRoles) {
        if(upRoles.getUpRoles().size() > 20) return Mono.error(badRequest());
        return Mono.just(upRoles.getUpRoles()
                .stream()
                .collect(Collectors.toMap(UpRole::getUsername, UpRole::getRole)));
    }

    public Mono<ChangeRoles> validateChangeRoles(ChangeRoles changeRoles) {
        return changeRoles.getChangeRoles().stream().anyMatch(changeRole ->
                doesRoleNotExist(changeRole.getOldRole()) || doesRoleNotExist(changeRole.getNewRole()))
                ? Mono.error(badRequest())
                : Mono.just(changeRoles);
    }

    public Mono<List<String>> validateUpdateRoles(Map<String, String> userRolesMap) {
        return userRolesMap.values().stream().anyMatch(RolesUtil::doesRoleNotExist)
                ? Mono.error(badRequest())
                : Mono.just("")
                .then(Mono.just(new ArrayList<>(userRolesMap.keySet())));
    }

    //public Mono<List<String>> getChangeRolesUsernames(ChangeRoles changeRoles) {
    //    return Mono.just(changeRoles.getChangeRoles()
    //            .stream()
    //            .map(ChangeRole::getUsername)
    //            .collect(Collectors.toList()));
    //}

    public Mono<Tuple2<UserRes, Map<String, ChangeRole>>> validateUserOldRole(Tuple2<UserRes, Map<String, ChangeRole>> tuple2) {
        Map<String, ChangeRole> changeRoleMap = tuple2.getT2();
        UserRes user = tuple2.getT1();
        Set<String> setUserRoles = user.getAllRoles()
                .stream()
                .map(RoleModel::getRole)
                .collect(Collectors.toSet());

        return !setUserRoles.contains(changeRoleMap.get(user.getUser()).getOldRole())
                ? Mono.error(badRequest())
                : Mono.just(tuple2);
    }

    //public Mono<UserRes> updateAccTokensForUser(UserRes user) {
//
    //    Mono<NewAccTokenData> androidNewAccTokenData = Mono.empty();
    //    Mono<NewAccTokenData> iosNewAccTokenData = Mono.empty();
    //    Mono<NewAccTokenData> webNewAccTokenData = Mono.empty();
    //    if (user.getAndroidJwtModel() != null) {
    //        androidNewAccTokenData = tokenService.genNewAccessToken(user, ANDROID);
    //    }
    //    if (user.getIosJwtModel() != null) {
    //        iosNewAccTokenData = tokenService.genNewAccessToken(user, IOS);
    //    }
    //    if (user.getWebJwtModel() != null) {
    //        webNewAccTokenData = tokenService.genNewAccessToken(user, WEB);
    //    }
//
    //    return Mono.zip(
    //            Mono.just(user),
    //            androidNewAccTokenData.defaultIfEmpty(NewAccTokenData.nullAccessToken()),
    //            iosNewAccTokenData.defaultIfEmpty(NewAccTokenData.nullAccessToken()),
    //            webNewAccTokenData.defaultIfEmpty(NewAccTokenData.nullAccessToken()))
    //            .flatMap(UserFactoryAllTokenRoles::updateUserAccTokensMono);
    //}

    public Mono<UserRes> updateUserWithNewRole(Tuple2<UserRes, Map<String, ChangeRole>> tuple2) {
        UserRes user = tuple2.getT1();
        ChangeRole changeRole = tuple2.getT2().get(user.getUser());
        String oldRole = changeRole.getOldRole();
        String newRole = changeRole.getNewRole();

        return ReactiveSecurityContextHolder.getContext()
                .flatMap(SecurityUtil::getRequesterDataMono)
                .flatMap(requester -> changeUserRoleMono(user, oldRole, newRole, requester.getUserId()));
    }

    public Flux<UserRes> saveAllUsersToMongoDb(List<UserRes> users) {
        return userRepository.saveAll(users);
    }

    public Mono<Tuple2<UserRes, Map<String, String>>> validateUserCanAddRole(Tuple2<UserRes, Map<String, String>> tuple2) {
        UserRes user = tuple2.getT1();
        String newRole = tuple2.getT2().get(user.getUser());

        return user.getAllRoles().size() >= 3 || user.getAllRoles()
                .stream()
                .anyMatch(roleModel-> roleModel.getRole().equals(newRole))
                ? Mono.error(badRequest())
                : Mono.just(tuple2);
    }

    public Mono<Tuple2<UserRes, Map<String, String>>> validateUserCanDelRole(Tuple2<UserRes, Map<String, String>> tuple2) {
        UserRes user = tuple2.getT1();
        String roleToDel = tuple2.getT2().get(user.getUser());

        return user.getAllRoles().size() <= 1 || user.getAllRoles()
                .stream()
                .noneMatch(roleModel-> roleModel.getRole().equals(roleToDel))
                ? Mono.error(badRequest())
                : Mono.just(tuple2);
    }

    public Mono<UserRes> updateUserWithAddedRole(Tuple2<UserRes, Map<String, String>> tuple2) {
        UserRes user = tuple2.getT1();
        String addedRole = tuple2.getT2().get(user.getUser());

        return ReactiveSecurityContextHolder.getContext()
                .flatMap(SecurityUtil::getRequesterDataMono)
                .flatMap(requester -> Mono.just(UserFactoryAddDelRole.getUserWithAddedRole(user, addedRole, requester.getUserId() )));
    }

    public Mono<UserRes> updateUserWithDelRole(Tuple2<UserRes, Map<String, String>> tuple2) {
        UserRes user = tuple2.getT1();
        String roleToDel = tuple2.getT2().get(user.getUser());

        return ReactiveSecurityContextHolder.getContext()
                .flatMap(SecurityUtil::getRequesterDataMono)
                .flatMap(requester -> Mono.just(UserFactoryAddDelRole.getUserWithDelRole(user, roleToDel)));
    }

    public Flux<Tuple2<UserRes, Map<String, String>>> validateRolesAndFetchUsers(Mono<Map<String, String>> userRolesMapMono) {
        return userRolesMapMono
                .flatMap(this::validateUpdateRoles)
                .flatMapMany(authServices::getUsersByUsername)
                .zipWith(userRolesMapMono);
    }

    //private Mono<Boolean> ff2(Mono<List<UserRes>> userListMono) {
    //    return userListMono
    //            .flatMapMany(this::saveAllUsersToMongoDb)
    //            .flatMap(adminChannelService::publishUserAuthOnRedis)
    //            .then(Mono.just(true));
    //}

    //public Mono<UserRes> saveAccTokensIdToRedis(UserRes user) {
    //    Mono<Boolean> androidRedisUpdate = Mono.empty();
    //    Mono<Boolean> iosRedisUpdate = Mono.empty();
    //    Mono<Boolean> webRedisUpdate = Mono.empty();
    //    if(user.getWebJwtModel() != null) {
    //        webRedisUpdate = tokenService.redisSetUserToken(user, user.getWebAccessTokenId(), WEB);
    //    }
    //    if(user.getAndroidJwtModel() != null) {
    //        androidRedisUpdate = tokenService.redisSetUserToken(user, user.getAndroidAccessTokenId(), ANDROID);
    //    }
    //    if(user.getIosJwtModel() != null) {
    //        iosRedisUpdate = tokenService.redisSetUserToken(user, user.getIosAccessTokenId(), IOS);
    //    }
//
    //    return Mono.zip(webRedisUpdate, androidRedisUpdate, iosRedisUpdate)
    //            .then(Mono.just(user));
    //}
}
