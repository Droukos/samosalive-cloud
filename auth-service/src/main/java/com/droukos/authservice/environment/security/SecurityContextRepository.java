package com.droukos.authservice.environment.security;

import com.droukos.authservice.environment.security.authentication.AuthenticationManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

  @NonNull private final AuthenticationManager authenticationManager;

  @Value("${jwt.prefix.bearer}")
  private String tokenPrefix;

  @Override
  public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Mono<SecurityContext> load(ServerWebExchange swe) {

    String authHeader = swe.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

    String authToken = null;
    if (authHeader != null && authHeader.startsWith(tokenPrefix))
      authToken = authHeader.replace(tokenPrefix, "").trim();
    else log.warn("couldn't find bearer string, will ignore the header.");

    return (authToken != null)
        ? this.authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(swe.getRequest(), authToken))
            .map(SecurityContextImpl::new)
        : Mono.empty();
  }
}
