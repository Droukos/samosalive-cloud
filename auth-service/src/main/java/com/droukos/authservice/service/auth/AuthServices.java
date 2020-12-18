package com.droukos.authservice.service.auth;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.RequesterRefTokenData;
import com.droukos.authservice.environment.dto.client.auth.UpdateEmail;
import com.droukos.authservice.environment.dto.client.auth.UpdatePassword;
import com.droukos.authservice.environment.dto.client.auth.UpdateRole;
import com.droukos.authservice.environment.dto.client.auth.login.LoginRequest;
import com.droukos.authservice.environment.enums.Regexes;
import com.droukos.authservice.environment.security.TokenService;
import com.droukos.authservice.environment.security.tokens.RefreshJwtService;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.repo.UserRepository;
import com.droukos.authservice.util.SecurityUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Predicate;

import static com.droukos.authservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@RequiredArgsConstructor
public class AuthServices {

  @NonNull private final UserRepository userRepository;
  @NonNull private final TokenService tokenService;
  @NonNull private final RefreshJwtService refreshJwtService;

  public Mono<UserRes> getUser(LoginRequest loginRequest) {
    Predicate<String> isEmail = userInput -> userInput.matches(Regexes.EMAIL.getRegex());
    Function<String, Mono<UserRes>> fetchUser =
        user -> isEmail.test(user)
                ? userRepository.findFirstByEmail(user)
                : userRepository.findFirstByUser(user);

    return fetchUser
        .apply(loginRequest.getUser().toLowerCase())
        .switchIfEmpty(Mono.error(badRequest("User does not exist")));
  }



  public Mono<UserRes> getUserById(String userid) {
      return userRepository.findFirstById(userid)
              .defaultIfEmpty(new UserRes())
              .flatMap(userRes -> userRes.getId() != null
                      ? Mono.just(userRes)
                      : Mono.error(badRequest("User not found")));
  }

   public Mono<UserRes> getUserFromSecurityContext(SecurityContext context) {

    return userRepository
        .findById(SecurityUtil.getRequesterUserId(context))
        .switchIfEmpty(Mono.error(badRequest("User does not exist")));
   }

   public Mono<RequesterRefTokenData> getRequesterRefreshData(ServerRequest request) {
    return Mono.just(tokenService.getRequesterRefreshData(request));
   }

   public Mono<UserRes> getUserFromHttpCookieRequest(ServerRequest request) {
    return userRepository
        .findFirstById(refreshJwtService.getUserIdClaim(tokenService.getRefreshToken(request)))
        .switchIfEmpty(Mono.error(badRequest("User can't be found")));
   }

  public Mono<UserRes> getUserByPathVarId(ServerRequest request) {
    return (request.pathVariables().size() > 0)
        ? userRepository
            .findFirstById(request.pathVariable("id"))
            .switchIfEmpty(Mono.error(badRequest("User not exists")))
        : Mono.error(badRequest("Not id given in path var"));
  }

  public Mono<LoginRequest> createLoginReqDto(ServerRequest request) {
    return request
        .bodyToMono(LoginRequest.class)
        .switchIfEmpty(Mono.error(badRequest("Empty Login Request")));
  }

  public Mono<UserRes> createUserDto(ServerRequest request) {
    return request
        .bodyToMono(UserRes.class)
        .switchIfEmpty(Mono.error(badRequest("Not empty user ")));
  }

  public Mono<UpdateRole> createUpdateRoleDto(ServerRequest request) {
    return request
        .bodyToMono(UpdateRole.class)
        .switchIfEmpty(Mono.error(badRequest("Not empty update role")));
  }

  public Mono<UpdatePassword> createUpdatePasswordDto(ServerRequest request) {
    return request
        .bodyToMono(UpdatePassword.class)
        .switchIfEmpty(Mono.error(badRequest("Bad Update Password Dto")));
  }

   public Mono<UpdateEmail> createUpdateEmailDto(ServerRequest request) {
    return request
        .bodyToMono(UpdateEmail.class)
        .switchIfEmpty(Mono.error(badRequest("Bad Update Email Dto")));
   }

  public boolean accessTokenDataIsNotEmpty(NewAccTokenData tokenData) {
    return tokenData.getUserId() != null;
  }

  public boolean isSameUserIdAsPathId(SecurityContext securityContext, ServerRequest request) {
    return SecurityUtil.getRequesterData(securityContext)
            .getUserId()
            .equals(request.pathVariable("id"));
  }
}
