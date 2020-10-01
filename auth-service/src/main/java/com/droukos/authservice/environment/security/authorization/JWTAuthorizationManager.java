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

@Component
@RequiredArgsConstructor
public class JWTAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

  @NonNull private final JwtService jwtService;
  @NonNull private final AuthorizationProvider authorizationProvider;

  public Mono<AuthorizationDecision> doAuthorization(Claims claims) {

    return Mono.just(new AuthorizationDecision(true));
  }

  @Override
  public Mono<AuthorizationDecision> check(
      Mono<Authentication> authenticationMono, AuthorizationContext context) {

    return authenticationMono
        .flatMap(
            authentication -> {
              authorizationProvider.runSec(context.getExchange().getRequest());
              return Mono.just(authentication);
            })
        .flatMap(authentication -> jwtService.fetchClaims(context))
        .flatMap(this::doAuthorization);
  }
}
