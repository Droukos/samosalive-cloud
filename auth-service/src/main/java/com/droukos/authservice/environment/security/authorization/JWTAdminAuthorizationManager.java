package com.droukos.authservice.environment.security.authorization;

import com.droukos.authservice.environment.security.JwtService;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.droukos.authservice.environment.constants.authorities.Roles.GENERAL_ADMIN;
import static com.droukos.authservice.environment.security.HttpExceptionFactory.unauthorized;

@Component("adminAuthorizationManager")
@RequiredArgsConstructor
public class JWTAdminAuthorizationManager
    implements ReactiveAuthorizationManager<AuthorizationContext> {

  @NonNull private final JwtService jwtService;

  public Mono<AuthorizationDecision> doAuthorization(Claims claims) {
    return Mono.just(new AuthorizationDecision(true));
  }

  @Override
  public Mono<AuthorizationDecision> check(
      Mono<Authentication> mono, AuthorizationContext context) {

    return mono.flatMap(this::isValidAdminRole)
        .flatMap(authentication -> jwtService.fetchClaims(context))
        .flatMap(this::doAuthorization);
  }

  private Mono<Authentication> isValidAdminRole(Authentication authentication) {

    return authentication.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals(GENERAL_ADMIN))
        ? Mono.just(authentication)
        : Mono.error(unauthorized("Admin only"));
  }
}
