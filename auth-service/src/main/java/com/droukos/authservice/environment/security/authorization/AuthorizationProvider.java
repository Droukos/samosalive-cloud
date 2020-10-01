package com.droukos.authservice.environment.security.authorization;

import com.droukos.authservice.environment.interfaces.core_services.ServiceInfo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.droukos.authservice.environment.security.HttpExceptionFactory.notFound;

@Component
@RequiredArgsConstructor
public class AuthorizationProvider {

  @NonNull private final Authorization authorization;

  public void runSec(ServerHttpRequest shr) {

    ServiceInfo serviceInfo = authorization.fetchServiceInfo(shr.getPath());

    if (serviceInfo == null) throw notFound("Service Info Not Found");
    authorization
        .getJwtClaims(shr)
        .flatMap(list -> Mono.just(list.get(1)).cast(String.class))
        .subscribe(
            token -> {
              if (serviceInfo.runSecurity()) {
                authorization.init(serviceInfo, token, shr.getPath()).run();
              }
            });
  }
}
