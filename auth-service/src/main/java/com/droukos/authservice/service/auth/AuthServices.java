package com.droukos.authservice.service.auth;

import com.droukos.authservice.environment.dto.client.auth.UpdateEmail;
import com.droukos.authservice.environment.dto.client.auth.UpdatePassword;
import com.droukos.authservice.environment.dto.client.auth.UpdateRole;
import com.droukos.authservice.environment.dto.client.auth.login.LoginRequest;
import com.droukos.authservice.environment.enums.Regexes;
import com.droukos.authservice.environment.security.JwtService;
import com.droukos.authservice.environment.security.TokenService;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.repo.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.droukos.authservice.environment.security.HttpExceptionFactory.badRequest;

@Service
@RequiredArgsConstructor
public class AuthServices {

  @NonNull private final UserRepository userRepository;
  @NonNull private final JwtService jwtService;
  @NonNull private final TokenService tokenService;
  private final BiConsumer<UserRes, ServerRequest> setServerRequestToUser =
      UserRes::setServerRequest;
  private final BiConsumer<UserRes, String> setUserDeviceToUser = UserRes::setUserDevice;

  public Mono<UserRes> getUser(LoginRequest loginRequest) {
    Predicate<String> isEmail = userInput -> userInput.matches(Regexes.EMAIL.getRegex());
    Function<String, Mono<UserRes>> fetchUser =
        user ->
            (isEmail.test(user)
                ? userRepository.findFirstByEmail(user)
                : userRepository.findFirstByUser(user));

    return fetchUser
        .apply(loginRequest.getUser().toLowerCase())
        .defaultIfEmpty(new UserRes())
        .flatMap(this::errorCaseUserNotExist)
        .doOnNext(user -> user.setServerRequest(loginRequest.getRequest()))
        .doOnNext(user -> user.setLoginRequest(loginRequest));
  }

  public Mono<UserRes> getUserFromRequest(ServerRequest request) {
    return userRepository
        .findById(jwtService.getUserIdClaim(request))
        .defaultIfEmpty(new UserRes())
        .flatMap(this::errorCaseUserNotExist)
        .doOnNext(userRes -> setServerRequestToUser.accept(userRes, request))
        .doOnNext(userRes -> setUserDeviceToUser.accept(userRes, jwtService.getDevice(request)));
  }

  public Mono<UserRes> getUserFromHttpCookieRequest(ServerRequest request) {
    String refToken = tokenService.getRefreshToken(request);
    return userRepository
        .findFirstById(jwtService.getUserIdClaim(refToken))
        .defaultIfEmpty(new UserRes())
        .flatMap(this::errorCaseUserNotExist)
        .doOnNext(userRes -> setServerRequestToUser.accept(userRes, request))
        .doOnNext(userRes -> setUserDeviceToUser.accept(userRes, jwtService.getDevice(refToken)))
        .doOnNext(userRes -> userRes.setRefreshToken(refToken));
  }

  public Mono<UserRes> getUserByPathVarId(ServerRequest request) {
    return (request.pathVariables().size() > 0)
        ? userRepository
            .findFirstById(request.pathVariable("id"))
            .defaultIfEmpty(new UserRes())
            .flatMap(this::errorCaseUserNotExist)
            .doOnNext(userRes -> setServerRequestToUser.accept(userRes, request))
            .doOnNext(userRes -> setUserDeviceToUser.accept(userRes, jwtService.getDevice(request)))
        : Mono.error(badRequest("Not id given in path var"));
  }

  private Mono<UserRes> errorCaseUserNotExist(UserRes user) {
    return user.getUser() == null
        ? Mono.error(badRequest("User does not exists"))
        : Mono.just(user);
  }

  public Mono<LoginRequest> createLoginReqDto(ServerRequest request) {
    return request
        .bodyToMono(LoginRequest.class)
        .defaultIfEmpty(new LoginRequest())
        .doOnNext(loginRequest -> loginRequest.setRequest(request));
  }

  public Mono<UserRes> createUserDto(ServerRequest request) {
    return request
        .bodyToMono(UserRes.class)
        .defaultIfEmpty(new UserRes())
        .flatMap(this::errorCaseUserNotExist);
  }

  public Mono<UserRes> createUpdateRoleDto(UserRes user) {
    return user.getServerRequest()
        .bodyToMono(UpdateRole.class)
        .defaultIfEmpty(new UpdateRole())
        .doOnNext(user::setUpdateRole)
        .then(Mono.just(user));
  }

  public Mono<UserRes> createUpdatePasswordDto(UserRes user) {
    return user.getServerRequest()
        .bodyToMono(UpdatePassword.class)
        .defaultIfEmpty(new UpdatePassword())
        .doOnNext(user::setUpdatePassword)
        .then(Mono.just(user));
  }

  public Mono<UserRes> createUpdateEmailDto(UserRes user) {
    return user.getServerRequest()
        .bodyToMono(UpdateEmail.class)
        .defaultIfEmpty(new UpdateEmail())
        .doOnNext(user::setUpdateEmail)
        .then(Mono.just(user));
  }
}
