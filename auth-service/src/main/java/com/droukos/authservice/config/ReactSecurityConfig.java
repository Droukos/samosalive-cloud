package com.droukos.authservice.config;

import com.droukos.authservice.environment.security.SecurityContextRepository;
import com.droukos.authservice.environment.security.authentication.AuthenticationManager;
import com.droukos.authservice.environment.security.authorization.JWTAuthorizationManager;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import static com.droukos.authservice.environment.constants.authorities.Roles.GENERAL_ADMIN;
import static com.droukos.authservice.environment.services.lvl1_services.EnAuthServices.*;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@AllArgsConstructor
public class ReactSecurityConfig {

  private final AuthenticationManager authenticationManager;
  private final SecurityContextRepository securityContextRepository;
  private final JWTAuthorizationManager jwtAuthorizationManager;

  @Bean
  public static BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
    String[] frontendPatterns =
        new String[] {
          "/",
          "/public/**",
          "/login",
          "/register",
          "/search",
          "/news",
          "/event",
          "/**/profile",
          "/**/edit",
          "/**/privacy"
        };
    String[] authPatterns =
        new String[] {
          LOGIN.getFullUrl(),
          SIGN_UP.getFullUrl(),
          CHECK_USERNAME.getFullUrl(),
          CHECK_EMAIL.getFullUrl(),
          USER_DATA.getFullUrl(),
          ACCESS_TOKEN.getFullUrl()
        };
    String[] permitPatterns = ArrayUtils.addAll(frontendPatterns, authPatterns);

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
            .pathMatchers(permitPatterns).permitAll()
            .pathMatchers(HttpMethod.OPTIONS).permitAll()
            .pathMatchers(PASSWORD_RESET.getFullUrl()).access(jwtAuthorizationManager)
            .pathMatchers(PASSWORD_CHANGE.getFullUrl()).access(jwtAuthorizationManager)
            .pathMatchers(EMAIL_CHANGE.getFullUrl()).access(jwtAuthorizationManager)
            .pathMatchers(REVOKE_TOKENS.getFullUrl()).access(jwtAuthorizationManager)
            .pathMatchers(LOGOUT.getFullUrl()).access(jwtAuthorizationManager)
            .pathMatchers(PUT_ROLE_ADD.getFullUrl()).hasAnyRole(GENERAL_ADMIN)
            .pathMatchers(PUT_ROLE_DEL.getFullUrl()).hasAnyRole(GENERAL_ADMIN)
            //.anyExchange().access(jwtAuthorizationManager)
            .and()
            .build();
  }
}
