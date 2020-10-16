package com.droukos.authservice.controller;

import com.droukos.authservice.service.auth.*;
import com.droukos.authservice.service.validator.auth.ValidatorFactory;
import com.droukos.authservice.util.DeviceDetector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class AuthHandlerHttp {

  @NonNull private final TokensService tokensService;
  @NonNull private final PasswordService passwordService;
  @NonNull private final RolesService rolesService;
  @NonNull private final LoginService loginService;
  @NonNull private final LogoutService logoutService;
  @NonNull private final AuthServices authServices;

  public Mono<ServerResponse> login(ServerRequest request) {
    String os = DeviceDetector.detectUserDeviceOs(request);

    return authServices.createLoginReqDto(request)
        .zipWhen(authServices::getUser)
        .flatMap(tuple2 ->
                Mono.zip(
                    loginService.validatePassword(tuple2),
                    loginService.createNewAccessToken(tuple2.getT2(), os),
                    loginService.createNewRefreshToken(tuple2.getT2(), os)
                )
        )
        .flatMap(loginService::saveAccessTokenIdToRedis)
        .flatMap(loginService::saveThisLastLogin)
        .flatMap(loginService::loggedInUser);
  }

  public Mono<ServerResponse> passwordChange(ServerRequest request) {

    return
            authServices.createUpdatePasswordDto(request)
            .doOnNext(ValidatorFactory::validatePasswordChange)
            .zipWith(authServices.getUserByPathVarId(request))
            .flatMap(passwordService::validateOldPassword)
            .flatMap(tuple ->
                    Mono.zip(
                            Mono.just(tuple.getT1()),
                            Mono.just(tuple.getT2()),
                            tokensService.genNewAccTokenToUser(tuple.getT2()),
                            tokensService.genNewRefTokenToUser(tuple.getT2())
                    )
            )
            .flatMap(passwordService::setNewAccessTokenIdToRedis)
            .flatMap(passwordService::saveChangedUserPassword)
            .flatMap(tokensService::setRefTokenOnHttpCookieAndAccessTokenOnBody);
  }

  public Mono<ServerResponse> userRoleAdd(ServerRequest request) {

    return authServices.createUpdateRoleDto(request)
            .zipWith(authServices.getUserByPathVarId(request))
            .doOnNext(rolesService::validateRoleAddUpdate)
            .flatMap(tuple ->
                    Mono.zip(
                            Mono.just(tuple.getT1()),
                            Mono.just(tuple.getT2()),
                            ReactiveSecurityContextHolder.getContext(),
                            tokensService.genNewAccTokenToUser(tuple.getT2())
                    )
            )
            .flatMap(tuple ->
                    authServices.isSameUserIdAsPathId(tuple.getT3(), request)
                            ? rolesService.itsSameZipAddedUserRoleAndTokens(tuple)
                            : rolesService.notSameZipAddedUserRoleAndTokens(tuple)
            )
            .flatMap(rolesService::saveUserToMongoDb)
            .flatMap(tuple ->
                    authServices.accessTokenDataIsNotEmpty(tuple.getT2())
                            ? rolesService.saveNewAccessTokenIdToRedis(tuple)
                            : rolesService.removeAllAccessTokenIdFromRedis(tuple)
            )
            .flatMap(accessTokenData ->
                    authServices.accessTokenDataIsNotEmpty(accessTokenData)
                            ? rolesService.newAccessTokenResponse(accessTokenData)
                            : rolesService.justMessageResponse()
            );
  }

  public Mono<ServerResponse> userRoleDel(ServerRequest request) {
    return authServices.createUpdateRoleDto(request)
            .zipWith(authServices.getUserByPathVarId(request))
            .doOnNext(rolesService::validateRoleDelUpdate)
            .flatMap(tuple ->
                    Mono.zip(
                            Mono.just(tuple.getT1()),
                            Mono.just(tuple.getT2()),
                            ReactiveSecurityContextHolder.getContext(),
                            tokensService.genNewAccTokenToUser(tuple.getT2())))
            .flatMap(tuple ->
                    authServices.isSameUserIdAsPathId(tuple.getT3(), request)
                            ? rolesService.itsSameZipRemovedUserRoleAndTokens(tuple)
                            : rolesService.notSameZipRemovedUserRoleAndTokens(tuple))
            .flatMap(rolesService::saveUserToMongoDb)
            .flatMap(tuple ->
                    authServices.accessTokenDataIsNotEmpty(tuple.getT2())
                            ? rolesService.saveNewAccessTokenIdToRedis(tuple)
                            : rolesService.removeAllAccessTokenIdFromRedis(tuple))
            .flatMap(accessTokenData ->
                    authServices.accessTokenDataIsNotEmpty(accessTokenData)
                            ? rolesService.newAccessTokenResponse(accessTokenData)
                            : rolesService.justMessageResponse()
            );
  }

  public Mono<ServerResponse> revokeTokens(ServerRequest request) {

    return authServices.getUserByPathVarId(request)
            .flatMap(user -> Mono.zip(
                    Mono.just(user),
                    ReactiveSecurityContextHolder.getContext(),
                    tokensService.genNewAccTokenToUser(user),
                    tokensService.genNewRefTokenToUser(user)
            ))
            .flatMap(tuple ->
                    authServices.isSameUserIdAsPathId(tuple.getT2(), request)
                            ? tokensService.itsSameDeleteAllTokensAndAddNew(tuple)
                            : tokensService.notSameDeleteAllTokens(tuple)
            )
            .flatMap(tokensService::saveUserToMongoDb)
            .flatMap(tuple ->
                    authServices.accessTokenDataIsNotEmpty(tuple.getT2())
                            ? tokensService.saveNewAccessTokenIdToRedis(tuple)
                            : tokensService.removeAllAccessTokenIdFromRedis(tuple)
            )
            .flatMap(tuple ->
                    authServices.accessTokenDataIsNotEmpty(tuple.getT2())
                            ? tokensService.setRefTokenOnHttpCookieAndAccessTokenOnBody(tuple)
                            : tokensService.justMessageRevoked()
            );
  }

  public Mono<ServerResponse> logout() {

    return ReactiveSecurityContextHolder.getContext()
            .flatMap(authServices::getUserFromSecurityContext)
            .zipWith(ReactiveSecurityContextHolder.getContext())
            .flatMap(logoutService::updateUser)
            .flatMap(logoutService::removeAccessTokenIdFromRedis)
            .flatMap(logoutService::saveUserToMongoDb)
            .then(logoutService.logoutUser());

  }

  public Mono<ServerResponse> accessToken(ServerRequest request) {

    return authServices.getUserFromHttpCookieRequest(request)
            .zipWith(authServices.getRequesterRefreshData(request))
            .flatMap(tokensService::validateUserRefreshToken)
            .flatMap(tuple2 ->
                Mono.zip(
                        Mono.just(tuple2.getT1()),
                        Mono.just(tuple2.getT2()),
                        tokensService.genNewAccTokenFromRefToUser(tuple2),
                        tokensService.genNewRefTokenFromRefToUser(tuple2)
                )
            )
            .flatMap(tokensService::checkCaseRefTokenIsExpiring)
            .flatMap(tokensService::updateUserWithAccTokenAndExpiringRefToken)
            .flatMap(tokensService::saveNewAccessTokenIdToRedis)
            .flatMap(tokensService::saveUserToMongoDb)
            .flatMap(tokensService::newAccessToken);
  }

  public Mono<ServerResponse> userData(ServerRequest request) {

      return authServices.getUserFromHttpCookieRequest(request)
              .zipWith(authServices.getRequesterRefreshData(request))
              .flatMap(tokensService::validateUserRefreshToken)
              .flatMap(tuple ->
                      Mono.zip(
                            Mono.just(tuple.getT1()),
                            tokensService.genNewAccTokenFromRefToUser(tuple)
                      )
              )
              .flatMap(tokensService::updateUserAccessToken)
              .flatMap(tokensService::saveNewAccessTokenIdToRedis)
              .flatMap(tokensService::saveUserToMongoDb)
              .flatMap(tokensService::buildUserData);
  }
}
