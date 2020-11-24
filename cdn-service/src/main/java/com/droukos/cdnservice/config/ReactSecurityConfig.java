package com.droukos.cdnservice.config;

import com.droukos.cdnservice.environment.security.SecurityContextRepository;
import com.droukos.cdnservice.environment.security.authentication.AuthenticationManager;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import static com.droukos.cdnservice.environment.constants.authorities.Roles.GENERAL_ADMIN;
import static com.droukos.cdnservice.environment.services.AedDeviceServices.PUT_AED_DEVICE_PICS;

@Configuration
@EnableRSocketSecurity
@EnableReactiveMethodSecurity
// @Profile(value = {"development", "production"})
@AllArgsConstructor
public class ReactSecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {

        return http.exceptionHandling()
                .authenticationEntryPoint(
                        (swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                .accessDeniedHandler(
                        (swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))
                .and()
                .cors().disable()
                .csrf().disable()
                .logout().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers(PUT_AED_DEVICE_PICS.getFullUrl()).hasAnyRole(GENERAL_ADMIN)
                .anyExchange().authenticated()
                .and()
                .build();
    }

    @Bean
    public PayloadSocketAcceptorInterceptor rSocketInterceptor(RSocketSecurity security) {
        return security
                .jwt(jwtSpec -> jwtSpec.authenticationManager(authenticationManager))
                // .simpleAuthentication(Customizer.withDefaults())
                .authorizePayload(
                        authorizePayloadsSpec ->
                                authorizePayloadsSpec.anyRequest().authenticated().anyExchange().permitAll())
                .build();
    }

    @Bean
    public RSocketMessageHandler messageHandler(RSocketStrategies rsocketStrategies) {
        RSocketMessageHandler rmh = new RSocketMessageHandler();
        rmh.getArgumentResolverConfigurer()
                .addCustomResolver(new AuthenticationPrincipalArgumentResolver());
        rmh.setRSocketStrategies(rsocketStrategies);
        return rmh;
    }
}
