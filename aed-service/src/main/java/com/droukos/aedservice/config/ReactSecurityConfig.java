package com.droukos.aedservice.config;

import com.droukos.aedservice.environment.security.authentication.AuthenticationManager;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

import static com.droukos.aedservice.environment.constants.authorities.Roles.GENERAL_ADMIN;

@Configuration
@EnableWebFluxSecurity
@EnableRSocketSecurity
@EnableReactiveMethodSecurity
@AllArgsConstructor
public class ReactSecurityConfig {

  private final AuthenticationManager authenticationManager;

  @Bean
  public static BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public PayloadSocketAcceptorInterceptor rSocketInterceptor(RSocketSecurity security) {
    return security
        .jwt(jwtSpec -> jwtSpec.authenticationManager(authenticationManager))
        .authorizePayload(
            authorizePayloadsSpec ->
                authorizePayloadsSpec
                        .route("aed.register.device").hasRole(GENERAL_ADMIN)
                    .anyRequest().authenticated()
                    .anyExchange().permitAll())
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
