package com.droukos.authservice.environment.security.authentication;

import com.droukos.authservice.config.jwt.ClaimsConfig;
import com.droukos.authservice.environment.security.tokens.AccessJwtService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

  @NonNull private final AccessJwtService accessJwtService;
  @NonNull private final ClaimsConfig claimsConfig;

  @Override
  @SuppressWarnings("unchecked")
  public Mono<Authentication> authenticate(Authentication authentication) {
    String authToken = authentication.getCredentials().toString();
    log.info("Auth token: {}", authToken);

    return accessJwtService
        .testToken(authToken)
        .flatMap(
            claims -> {
              String username = claims.getSubject();
              List<String> roles = claims.get(claimsConfig.getAuthorities(), List.class);
              log.info("Expiration: {}", claims.getExpiration());
              log.info("TokenId: {}", claims.get(claimsConfig.getTokenId(), String.class));
              log.info("UserID: {}", claims.get(claimsConfig.getUserId(), String.class));
              log.info("User: {}", username);
              log.info("Roles: {}", roles);
              List<SimpleGrantedAuthority> authorities =
                  roles.stream()
                      .map(role -> "ROLE_" + role)
                      .map(SimpleGrantedAuthority::new)
                      .collect(Collectors.toList());
              UsernamePasswordAuthenticationToken auth =
                  new UsernamePasswordAuthenticationToken(
                      authToken, authentication.getPrincipal(), authorities);
              ReactiveSecurityContextHolder.getContext()
                  .doOnNext(
                      securityContext ->
                          securityContext.setAuthentication(
                              new AuthenticatedUser(authToken, authorities)));
              return Mono.just(auth);
            });
  }
}
