package com.droukos.userservice.service.user;


import com.droukos.userservice.environment.constants.PrivacyCodes;
import com.droukos.userservice.environment.dto.RequesterAccessTokenData;
import com.droukos.userservice.environment.dto.client.user.PreviewUserDto;
import com.droukos.userservice.environment.dto.client.user.UpdateUserPersonal;
import com.droukos.userservice.environment.dto.server.user.RequestedUsernameOnly;
import com.droukos.userservice.model.user.UserRes;
import com.droukos.userservice.model.user.privacy.PrivacySettingMap;
import com.droukos.userservice.repo.UserRepository;
import com.droukos.userservice.util.RolesUtil;
import com.droukos.userservice.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.*;

import static com.droukos.userservice.environment.security.HttpExceptionFactory.badRequest;

@Service
@AllArgsConstructor
public class UserInfoService {

    private final UserRepository userRepository;

    private final BiPredicate<RequesterAccessTokenData, UserRes> hasSameUserId = (requesterData, requestedUser) ->
            requesterData.getUserId().equals(requestedUser.getId());
    private final Predicate<RequesterAccessTokenData> isAnyValidAdmin = requesterData ->
            RolesUtil.hasAnyAdminRole(requesterData.getRoles());

    public Mono<Tuple2<UserRes, Set<String>>> getRequestedUserFiltersOnPrivacySettings(UserRes user) {

        return ReactiveSecurityContextHolder.getContext()
                .flatMap(context -> Mono.just(SecurityUtil.getRequesterData(context)))
                .flatMap(requesterTokenData->
                        (hasSameUserId.test(requesterTokenData, user) || (isAnyValidAdmin).test(requesterTokenData))
                                ? Mono.zip(Mono.just(user), Mono.just(Set.of()))
                                : Mono.zip(Mono.just(user), Mono.just(buildUserFilters(requesterTokenData, user)))
                );

    }

    public Mono<UpdateUserPersonal> checkRequesterUserIdOrRole(Tuple2<UpdateUserPersonal, SecurityContext> tuple2) {

        RequesterAccessTokenData requesterAccessTokenData = SecurityUtil.getRequesterData(tuple2.getT2());
        UpdateUserPersonal updateUserPersonal = tuple2.getT1();
        Function<UpdateUserPersonal, Mono<UpdateUserPersonal>> checkAdminRole = updateUserPersonal1 ->
                RolesUtil.hasAnyAdminRole(requesterAccessTokenData.getRoles())
                        ? Mono.just(updateUserPersonal) : Mono.error(badRequest());

        return !requesterAccessTokenData.getUserId().equals(updateUserPersonal.getUserid())
                ? checkAdminRole.apply(updateUserPersonal)
                : Mono.just(tuple2.getT1());
    }

    public Mono<Tuple2<UserRes, Set<String>>> getRequestedUsersFilterOnPrivSettings(Tuple2<UserRes, SecurityContext> tuple2) {
        UserRes requestedUser = tuple2.getT1();
        RequesterAccessTokenData requesterTokenData = SecurityUtil.getRequesterData(tuple2.getT2());

        return hasSameUserId.test(requesterTokenData, requestedUser) || (isAnyValidAdmin).test(requesterTokenData)
                ? Mono.zip(Mono.just(requestedUser), Mono.just(Set.of()))
                : Mono.zip(Mono.just(requestedUser), Mono.just(buildUserFilters(requesterTokenData, requestedUser)));
    }

    private Set<String> buildUserFilters(RequesterAccessTokenData requesterTokenData, UserRes requestedUser) {
        Set<String> fieldsToNullify = new HashSet<>();
        String authUsername = requesterTokenData.getUsername();
        Predicate<String> isUsernameIncluded = listedUser -> listedUser.equals(authUsername);

        BiConsumer<List<String>, String> notToCons = (users, field) -> users.stream()
                .filter(isUsernameIncluded)
                .findFirst()
                .ifPresent(e -> fieldsToNullify.add(field));
        BiConsumer<List<String>, String> onlyToCons = (users, field) -> users.stream()
                .filter(isUsernameIncluded)
                .findFirst()
                .ifPresentOrElse(e -> {
                }, () -> fieldsToNullify.add(field));

        Consumer<PrivacySettingMap> buildFieldsToNullify = privySetting ->
                privySetting.getPrivySets().forEach((field, privacySetting) -> {
                    switch (privacySetting.getType()) {
                        case PrivacyCodes.PRIVATE -> fieldsToNullify.add(field);
                        case PrivacyCodes.NOT_TO -> notToCons.accept(privacySetting.getUsers(), field);
                        case PrivacyCodes.ONLY_TO -> onlyToCons.accept(privacySetting.getUsers(), field);
                    }
                });

        buildFieldsToNullify.accept(requestedUser.getPrivy());
        return fieldsToNullify;
    }


    public Flux<UserRes> fetchPreviewUsers(PreviewUserDto previewUserDto) {
        String username = previewUserDto.getUsername();
        List<String> filterRoles = previewUserDto.getFilterRoles();
        if (username == null) {
            if (filterRoles != null && filterRoles.size() != 0) {
                return switch (filterRoles.size()) {
                    case 1 -> userRepository.findUsersWithRole(filterRoles.get(0));
                    case 2 -> userRepository.findUsersWith2Roles(filterRoles.get(0), filterRoles.get(1));
                    case 3 -> userRepository.findUsersWith3Roles(filterRoles.get(0), filterRoles.get(1), filterRoles.get(2));
                    case 4 -> userRepository.findUsersWith4Roles(filterRoles.get(0), filterRoles.get(1), filterRoles.get(2), filterRoles.get(3));
                    case 5 -> userRepository.findUsersWith5Roles(filterRoles.get(0), filterRoles.get(1), filterRoles.get(2), filterRoles.get(3), filterRoles.get(4));
                    default -> Flux.error(badRequest());
                };
            }
        } else {
            username = username.toLowerCase();
            if (filterRoles !=null && filterRoles.size() != 0) {
                return switch (filterRoles.size()) {
                    case 1 -> userRepository.findUsersWithUsernameLikeAndRole(username, filterRoles.get(0));
                    case 2 -> userRepository.findUsersWithUsernameLikeAnd2Roles(username, filterRoles.get(0), filterRoles.get(1));
                    case 3 -> userRepository.findUsersWithUsernameLikeAnd3Roles(username, filterRoles.get(0), filterRoles.get(1), filterRoles.get(2));
                    case 4 -> userRepository.findUsersWithUsernameLikeAnd4Roles(username, filterRoles.get(0), filterRoles.get(1), filterRoles.get(2), filterRoles.get(3));
                    case 5 -> userRepository.findUsersWithUsernameLikeAnd5Roles(username, filterRoles.get(0), filterRoles.get(1), filterRoles.get(2), filterRoles.get(3), filterRoles.get(4));
                    default -> Flux.error(badRequest());
                };
            } else {
                return userRepository.findUserByUserLike(username.toLowerCase());
            }
        }
        return Flux.error(badRequest());
    }

    public Flux<String> fetchUsernamesLike(String username) {
        return userRepository.findByUserLike(username.toLowerCase())
                //.take(1)
                .map(RequestedUsernameOnly::getUser);
    }
}
