package com.droukos.authservice.service.auth;

import com.droukos.authservice.environment.security.JwtService;
import com.droukos.authservice.environment.security.TokenService;
import com.droukos.authservice.model.user.Role;
import com.droukos.authservice.model.user.UserRes;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.droukos.authservice.service.validator.auth.ValidatorFactory.validateAddRole;
import static com.droukos.authservice.service.validator.auth.ValidatorFactory.validateDelRole;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@RequiredArgsConstructor
public class RolesService {

  @NonNull private final TokenService tokenService;
  @NonNull private final JwtService jwtService;
  @NonNull private final TokensService tokensService;

  public void validateRoleAddUpdate(UserRes user) {
    validateAddRole(user.getUpdateRole());
  }

  public void validateRoleDelUpdate(UserRes user) {
    validateDelRole(user.getUpdateRole());
  }

  public void setAllRolesFromDbToDto(UserRes user) {
    user.getUpdateRole()
        .setRolesOnDb(user.getAllRoles().stream().map(Role::getCode).collect(Collectors.toList()));
  }

  public void addNewRoleToUser(UserRes user) {
    user.getAllRoles()
        .add(
            Role.build(
                user.getUpdateRole().getUpdatedRole(),
                jwtService.getUsername(user.getServerRequest())));
  }

  public void removeSpecifiedRoleFromUser(UserRes user) {
    user.getAllRoles().stream()
        .filter(role -> role.getCode().equals(user.getUpdateRole().getUpdatedRole()))
        .findFirst()
        .ifPresent(role -> user.getAllRoles().remove(role));
  }

  public Mono<ServerResponse> saveUserRole(UserRes user) {
    String newAccessTokenForUser = tokenService.deleteAccessTokensForUser(user);
    Predicate<String> sameUser = Objects::nonNull;

    return tokensService
        .setNewAccessTokenIdToRedis(user)
        .flatMap(tokensService::saveUserTokenChangesOnDb)
        .doOnNext(tokensService::setHttpCookieRefTokenToUser)
        .flatMap(
            savedUser ->
                sameUser.test(newAccessTokenForUser)
                    ? tokensService.setRefTokenOnHttpCookieAndAccessTokenOnBody(savedUser)
                    : ok().body(fromValue("user.role.updated")));
  }
}
