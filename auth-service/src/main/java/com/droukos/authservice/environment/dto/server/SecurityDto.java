package com.droukos.authservice.environment.dto.server;

import com.droukos.authservice.environment.interfaces.core_services.ServiceInfo;
import lombok.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

import java.util.List;

@NoArgsConstructor
@Data
@ToString
@Setter
@Getter
public class SecurityDto {
    private ServerHttpRequest httpRequest;
    private Authentication authentication;
    private AuthorizationContext authorizationContext;
    private ServiceInfo serviceInfo;
    private String userId;
    private String pathVarId;
    private String accToken;
    private List<String> roles;

    public Mono<SecurityDto> setAuthenticationToMonoSecDto(Authentication authentication) {
        setAuthentication(authentication);
        return Mono.just(this);
    }
}
