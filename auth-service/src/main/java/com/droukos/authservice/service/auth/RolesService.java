package com.droukos.authservice.service.auth;

import com.droukos.authservice.environment.dto.RequesterAccessTokenData;
import com.droukos.authservice.environment.dto.server.auth.token.NewAccessTokenResponse;
import com.droukos.authservice.environment.security.TokenService;
import com.droukos.authservice.model.user.RoleModel;
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
import static java.time.LocalDateTime.now;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@RequiredArgsConstructor
public class RolesService {

  @NonNull private final TokenService tokenService;
  @NonNull private final TokensService tokensService;

  public void validateRoleAddUpdate(UserRes user) {
    validateAddRole(user.getUpdateRole());
  }

  public void validateRoleDelUpdate(UserRes user) {
    validateDelRole(user.getUpdateRole());
  }

  public void setAllRolesFromDbToDto(UserRes user) {
    user.getUpdateRole()
        .setRolesOnDb(
            user.getAllRoles().stream().map(RoleModel::getRole).collect(Collectors.toList()));
  }

  public void addNewRoleToUser(UserRes user) {
    user.getAllRoles()
        .add(
            RoleModel.builder()
                .role(user.getUpdateRole().getUpdatedRole())
                .active(false)
                .added(now())
                .addedBy(user.getRequesterAccessTokenData().getUsername())
                .build());
  }

  public void removeSpecifiedRoleFromUser(UserRes user) {
    user.getAllRoles().stream()
        .filter(role -> role.getRole().equals(user.getUpdateRole().getUpdatedRole()))
        .findFirst()
        .ifPresent(role -> user.getAllRoles().remove(role));
  }

  public Mono<ServerResponse> saveUserRole(UserRes user) {
    Predicate<RequesterAccessTokenData> sameUser = Objects::nonNull;
    tokenService.deleteAccessTokensForUser(user);

    return tokensService.saveUserTokenChangesOnDb(user)
        .flatMap(
            savedUser ->
                sameUser.test(user.getRequesterAccessTokenData())
                    ? tokensService
                        .setNewAccessTokenIdToRedis(user)
                        .then(
                            ok().body(fromValue(
                                        NewAccessTokenResponse.builder()
                                            .accessToken(user.getRequesterAccessTokenData().getToken())
                                            .build())))
                    : ok().body(fromValue("user.role.updated")));
  }
}
