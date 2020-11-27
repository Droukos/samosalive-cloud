package com.droukos.userservice.service.user;


import com.droukos.userservice.environment.constants.PrivacyCodes;
import com.droukos.userservice.environment.dto.RequesterAccessTokenData;
import com.droukos.userservice.environment.dto.client.user.UpdateUserPersonal;
import com.droukos.userservice.environment.dto.server.user.RequestedUsernameOnly;
import com.droukos.userservice.environment.security.AccessJwtService;
import com.droukos.userservice.model.user.UserRes;
import com.droukos.userservice.model.user.privacy.PrivacySettingMap;
import com.droukos.userservice.repo.UserRepository;
import com.droukos.userservice.util.RolesUtil;
import com.droukos.userservice.util.SecurityUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
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
@Lazy
@RequiredArgsConstructor
public class UserInfoService {

  @NonNull
  private final UserRepository userRepository;
  @NonNull
  private final AccessJwtService accessJwtService;

  private final BiPredicate<RequesterAccessTokenData, UserRes> hasSameUserId = (requesterData, requestedUser) ->
          requesterData.getUserId().equals(requestedUser.getId());
  private final Predicate<RequesterAccessTokenData> isAnyValidAdmin = requesterData ->
          RolesUtil.hasAnyAdminRole(requesterData.getRoles());





  public Mono<Tuple2<UserRes,Set<String>>> getRequestedUserFiltersOnPrivacySettings(UserRes user) {

   return ReactiveSecurityContextHolder.getContext()
           .flatMap(context -> {
              RequesterAccessTokenData requesterTokenData = SecurityUtil.getRequesterData(context);

              if (hasSameUserId.test(requesterTokenData, user) || (isAnyValidAdmin).test(requesterTokenData)) {
                return Mono.zip(Mono.just(user),Mono.just(Set.of()));
              }
              return Mono.zip(Mono.just(user), Mono.just(buildUserFilters(requesterTokenData, user)));
            });

  }

  public Mono<UpdateUserPersonal> checkRequesterUserIdOrRole(Tuple2<UpdateUserPersonal, SecurityContext> tuple2) {

      RequesterAccessTokenData requesterAccessTokenData = SecurityUtil.getRequesterData(tuple2.getT2());
      UpdateUserPersonal updateUserPersonal = tuple2.getT1();
      Function<UpdateUserPersonal, Mono<UpdateUserPersonal>> checkAdminRole = updateUserPersonal1 ->
              RolesUtil.hasAnyAdminRole(requesterAccessTokenData.getRoles())
                      ? Mono.just(updateUserPersonal) : Mono.error(badRequest());

      return (!requesterAccessTokenData.getUserId().equals(updateUserPersonal.getUserid()))
              ? checkAdminRole.apply(updateUserPersonal)
              : Mono.just(tuple2.getT1());
  }

  public Mono<Tuple2<UserRes, Set<String>>> getRequestedUsersFilterOnPrivSettings(Tuple2<UserRes, SecurityContext> tuple2) {
    UserRes requestedUser = tuple2.getT1();
    RequesterAccessTokenData requesterTokenData = SecurityUtil.getRequesterData(tuple2.getT2());

    if (hasSameUserId.test(requesterTokenData, requestedUser) || (isAnyValidAdmin).test(requesterTokenData)) {
      return Mono.zip(Mono.just(requestedUser),Mono.just(Set.of()));
    }
    return Mono.zip(Mono.just(requestedUser), Mono.just(buildUserFilters(requesterTokenData, requestedUser)));
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


  public Flux<UserRes> fetchPreviewUsers(String username) {
    return userRepository.findUserByUserLike(username.toLowerCase());
  }

  public Flux<String> fetchUsernamesLike(String username) {
    return userRepository.findByUserLike(username.toLowerCase())
            //.take(1)
            .map(RequestedUsernameOnly::getUser);
  }
}
