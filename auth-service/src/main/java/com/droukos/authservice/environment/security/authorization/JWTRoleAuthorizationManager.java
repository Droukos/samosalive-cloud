package com.droukos.authservice.environment.security.authorization;

import com.droukos.authservice.environment.interfaces.JWTReactiveAuthorizationManager;
import com.droukos.authservice.environment.security.JwtService;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JWTRoleAuthorizationManager extends JWTReactiveAuthorizationManager {

  @NonNull
  private final JwtService jwtService;

  @Override
  public JwtService getJwtService() {
    return this.jwtService;
  }

  @Override
  public Mono<AuthorizationDecision> doAuthorization(Claims claims) {
    return Mono.just(new AuthorizationDecision(true));
  }

}
