package com.droukos.userservice.environment.interfaces;

import com.droukos.userservice.environment.security.AccessJwtService;
import io.jsonwebtoken.Claims;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public abstract class JWTReactiveAuthorizationManager implements ReactiveAuthorizationManager<ApplicationContext> {

  @Override
  public Mono<AuthorizationDecision> check(Mono<Authentication> monoAuthentication,
      ApplicationContext context) {

    return monoAuthentication
        .flatMap(authentication -> Mono.just(authentication.getCredentials().toString()))
        .flatMap(token -> context.getBean(AccessJwtService.class).testToken(token))
        .flatMap(this::doAuthorization);

  }

  public abstract AccessJwtService getJwtService();
  public abstract Mono<AuthorizationDecision> doAuthorization(Claims claims);
}
