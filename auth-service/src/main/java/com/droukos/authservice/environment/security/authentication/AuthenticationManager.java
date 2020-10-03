package com.droukos.authservice.environment.security.authentication;

import com.droukos.authservice.environment.security.JwtService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

  @NonNull private final JwtService jwtService;

  @Value("${jwt.authorities_key}")
  private String authoritiesKey;

  @Value("${jwt.user_id}")
  private String userIdClaim;

  @Override
  @SuppressWarnings("unchecked")
  public Mono<Authentication> authenticate(Authentication authentication) {
    String authToken = authentication.getCredentials().toString();
    log.info("Auth token: {}", authToken);

    return jwtService
        .testToken(authToken)
        .flatMap(
            claims -> {
              String username = claims.getSubject();
              List<String> roles = claims.get(authoritiesKey, List.class);
              log.info("Expiration: {}", jwtService.getExpDate(authToken));
              log.info("TokenId: {}", jwtService.getTokenId(authToken));
              log.info("UserID: {}", claims.get(userIdClaim, String.class));
              log.info("User: {}", username);
              log.info("Roles: {}", roles);
              List<SimpleGrantedAuthority> authorities =
                  roles.stream().map(role -> "ROLE_"+ role).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
              UsernamePasswordAuthenticationToken auth =
                  new UsernamePasswordAuthenticationToken(
                      authToken, authentication.getPrincipal(), authorities);
              SecurityContextHolder.getContext()
                  .setAuthentication(new AuthenticatedUser(username, authorities));
              return Mono.just(auth);
            });
  }
}
