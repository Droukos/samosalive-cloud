package com.droukos.aedservice.environment.security.authentication;

import com.droukos.aedservice.config.jwt.ClaimsConfig;
import com.droukos.aedservice.environment.dto.RequesterAccessTokenData;
import com.droukos.aedservice.environment.security.tokens.AccessJwtService;
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

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @NonNull
    private final AccessJwtService accessJwtService;
    @NonNull
    private final ClaimsConfig claimsConfig;

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
                            Date exp = claims.getExpiration();
                            String tokenId = claims.get(claimsConfig.getTokenId(), String.class);
                            String userId = claims.get(claimsConfig.getUserId(), String.class);
                            String userDevice = claims.get(claimsConfig.getPlatform(), String.class);
                            log.info("Expiration: {}", exp);
                            log.info("TokenId: {}", tokenId);
                            log.info("UserID: {}", userId);
                            log.info("User: {}", username);
                            log.info("Roles: {}", roles);

                            List<SimpleGrantedAuthority> authorities =
                                    roles.stream()
                                            .map(role -> "ROLE_" + role)
                                            .map(SimpleGrantedAuthority::new)
                                            .collect(Collectors.toList());
                            RequesterAccessTokenData requesterData =
                                    new RequesterAccessTokenData(
                                            authToken, tokenId, userId, username, userDevice, exp, roles);
                            UsernamePasswordAuthenticationToken auth =
                                    new UsernamePasswordAuthenticationToken(
                                            requesterData, authentication.getPrincipal(), authorities);
                            ReactiveSecurityContextHolder.getContext()
                                    .doOnNext(
                                            securityContext ->
                                                    securityContext.setAuthentication(
                                                            new AuthenticatedUser(requesterData, authorities)));

                            return Mono.just(auth);
                        });
    }
}
