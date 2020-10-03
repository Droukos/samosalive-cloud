package com.droukos.authservice.controller;

import com.droukos.authservice.service.auth.*;
import com.droukos.authservice.util.DeviceDetector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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

    return authServices
        .createLoginReqDto(request)
        .flatMap(authServices::getUser)
        .flatMap(loginService::validatePassword)
        .doOnNext(DeviceDetector::detectUserDeviceOs)
        .doOnNext(loginService::createNewAccessToken)
        .doOnNext(loginService::createNewRefreshToken)
        .flatMap(loginService::saveAccessTokenIdToRedis)
        .doOnNext(loginService::setUserIsNowOnline)
        .doOnNext(loginService::saveThisLastLogin)
        .doOnNext(loginService::setUserNewTokens)
        .flatMap(loginService::loggedInUser);
  }

  public Mono<ServerResponse> passwordChange(ServerRequest request) {

    return authServices
        .getUserByPathVarId(request)
        .flatMap(authServices::createUpdatePasswordDto)
        .doOnNext(passwordService::addEncodedPasswordFromDbToDto)
        .flatMap(passwordService::validateNewPassword)
        .doOnNext(passwordService::encodeNewPasswordToUser)
        .doOnNext(tokensService::genNewAccTokenToUser)
        .doOnNext(tokensService::genNewRefTokenToUser)
        .flatMap(tokensService::setNewAccessTokenIdToRedis)
        .flatMap(tokensService::saveUserTokenChangesOnDb)
        .flatMap(tokensService::setRefTokenOnHttpCookieAndAccessTokenOnBody);
  }

  public Mono<ServerResponse> userRoleAdd(ServerRequest request) {

    return authServices
        .getUserByPathVarId(request)
        .flatMap(authServices::createUpdateRoleDto)
        .doOnNext(rolesService::setAllRolesFromDbToDto)
        .doOnNext(rolesService::validateRoleAddUpdate)
        .doOnNext(rolesService::addNewRoleToUser)
        .flatMap(rolesService::saveUserRole);
  }

  public Mono<ServerResponse> userRoleDel(ServerRequest request) {

    return authServices
        .getUserByPathVarId(request)
        .flatMap(authServices::createUpdateRoleDto)
        .doOnNext(rolesService::setAllRolesFromDbToDto)
        .doOnNext(rolesService::validateRoleDelUpdate)
        .doOnNext(rolesService::removeSpecifiedRoleFromUser)
        .flatMap(rolesService::saveUserRole);
  }

  public Mono<ServerResponse> revokeTokens(ServerRequest request) {

    return authServices
        .getUserByPathVarId(request)
        .doOnNext(tokensService::genNewAccTokenToUser)
        .doOnNext(tokensService::genNewRefTokenToUser)
        .doOnNext(tokensService::deleteAllOldJwtTokens)
        .doOnNext(tokensService::setUserNewTokens)
        .flatMap(tokensService::setNewAccessTokenIdToRedis)
        .flatMap(tokensService::saveUserTokenChangesOnDb)
        .flatMap(tokensService::setRefTokenOnHttpCookieAndAccessTokenOnBody);
  }

  public Mono<ServerResponse> logout(ServerRequest request) {

    return authServices
        .getUserFromRequest(request)
        .doOnNext(logoutService::nullifyUserJwtModel)
        .doOnNext(logoutService::caseNoJwtModelsLeftSetUserOffline)
        .doOnNext(logoutService::saveThisLogoutTime)
        .flatMap(logoutService::removeAccessTokenIdFromRedis)
        .flatMap(logoutService::logoutUser);
  }

  public Mono<ServerResponse> accessToken(ServerRequest request) {

    return authServices
        .getUserFromHttpCookieRequest(request)
        .flatMap(tokensService::validateUserRefreshToken)
        .doOnNext(tokensService::genNewAccTokenToUser)
        .doOnNext(tokensService::genNewRefTokenToUser)
        .doOnNext(tokensService::checkCaseRefreshTokenIsExpiring)
        .flatMap(tokensService::setNewAccessTokenIdToRedis)
        .flatMap(tokensService::saveUserTokenChangesOnDb)
        .flatMap(tokensService::newAccessToken);
  }

  public Mono<ServerResponse> userData(ServerRequest request) {

    return authServices
        .getUserFromHttpCookieRequest(request)
        .flatMap(tokensService::validateUserRefreshToken)
        .doOnNext(tokensService::setUserDeviceFromCookieRefTokenToUser)
        .doOnNext(tokensService::genNewAccTokenToUser)
        .doOnNext(tokensService::setAccessTokenModel)
        .flatMap(tokensService::setNewAccessTokenIdToRedis)
        .flatMap(tokensService::saveUserTokenChangesOnDb)
        .flatMap(tokensService::buildUserData);
  }
}
