package com.droukos.authservice.service.auth;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.RequesterAccessTokenData;
import com.droukos.authservice.environment.dto.client.auth.UpdateRole;
import com.droukos.authservice.environment.dto.server.auth.token.NewAccessTokenResponse;
import com.droukos.authservice.environment.security.TokenService;
import com.droukos.authservice.model.factories.user.res.role_tokens.UserFactoryAllTokenRoles;
import com.droukos.authservice.model.factories.user.res.role_tokens.UserFactoryAndroidTokenRole;
import com.droukos.authservice.model.factories.user.res.role_tokens.UserFactoryIosTokenRole;
import com.droukos.authservice.model.factories.user.res.role_tokens.UserFactoryWebTokenRole;
import com.droukos.authservice.model.user.RoleModel;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.repo.UserRepository;
import com.droukos.authservice.util.SecurityUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple4;

import java.util.stream.Collectors;

import static com.droukos.authservice.environment.constants.Platforms.*;
import static com.droukos.authservice.service.validator.auth.ValidatorFactory.validateAddRole;
import static com.droukos.authservice.service.validator.auth.ValidatorFactory.validateDelRole;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@RequiredArgsConstructor
public class RolesService {

  @NonNull private final  UserRepository userRepository;
  @NonNull private final TokenService tokenService;

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
                      case IOS    ->  UserFactoryIosTokenRole.addRoleIosAccessTokens(user, tokenData, newRole, addedBy);
                      case WEB    ->  UserFactoryWebTokenRole.addRoleWebAccessTokens(user, tokenData, newRole, addedBy);
                      default     ->  UserFactoryAndroidTokenRole.addRoleAndroidAccessTokens(user, tokenData, newRole, addedBy);
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
                      case IOS    ->  UserFactoryIosTokenRole.removedRoleIosAccessTokens(user, tokenData, newRole);
                      case WEB    ->  UserFactoryWebTokenRole.removedRoleWebAccessTokens(user, tokenData, newRole);
                      default     ->  UserFactoryAndroidTokenRole.removedRoleAndroidAccessTokens(user, tokenData, newRole);
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

  public  Mono<Tuple2<UserRes, NewAccTokenData>> saveUserToMongoDb(Tuple2<UserRes, NewAccTokenData> tuple2) {
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
}
