package com.droukos.authservice.environment.interfaces;

import com.droukos.authservice.environment.security.JwtService;
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
        .flatMap(token -> context.getBean(JwtService.class).testToken(token))
        .flatMap(this::doAuthorization);

  }

  public abstract JwtService getJwtService();
  public abstract Mono<AuthorizationDecision> doAuthorization(Claims claims);
}
