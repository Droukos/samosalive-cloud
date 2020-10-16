package com.droukos.userservice.config;

import com.droukos.userservice.environment.security.authentication.AuthenticationManager;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import lombok.AllArgsConstructor;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder;
import org.springframework.util.MimeTypeUtils;

import static com.droukos.userservice.environment.constants.authorities.Roles.GENERAL_ADMIN;

@Configuration
@EnableRSocketSecurity
@EnableReactiveMethodSecurity
// @Profile(value = {"development", "production"})
@AllArgsConstructor
public class ReactSecurityConfig {

  private final AuthenticationManager authenticationManager;

 //@Bean
 //public RSocket rSocket() {
 //  return RSocketFactory
 //          .connect()
 //          .dataMimeType(MimeTypeUtils.APPLICATION_JSON_VALUE)
 //          .frameDecoder(PayloadDecoder.ZERO_COPY)
 //          .transport(WebsocketClientTransport.create(8985))
 //          .start().block();
 //}


  @Bean
  public PayloadSocketAcceptorInterceptor rSocketInterceptor(RSocketSecurity security) {
    return security
        .jwt(jwtSpec -> jwtSpec.authenticationManager(authenticationManager))
            //.simpleAuthentication(Customizer.withDefaults())
        .authorizePayload(
            authorizePayloadsSpec -> authorizePayloadsSpec
                    .anyRequest().authenticated()
                    .anyExchange().permitAll()
        )
        .build();
  }

  //@Bean
  //public RSocketStrategies rsocketStrategies() {
  //  return RSocketStrategies.builder()
  //          .decoder(new Jackson2CborDecoder())
  //          .encoder(new Jackson2CborEncoder())
  //          .build();
  //}

  @Bean
  public RSocketMessageHandler messageHandler(RSocketStrategies rsocketStrategies) {
    RSocketMessageHandler rmh = new RSocketMessageHandler();
    rmh.getArgumentResolverConfigurer()
        .addCustomResolver(new AuthenticationPrincipalArgumentResolver());
    rmh.setRSocketStrategies(rsocketStrategies);
    return rmh;
  }

  //@Bean
  //MapReactiveUserDetailsService authentication() {
  //  return new MapReactiveUserDetailsService(
  //      User.withDefaultPasswordEncoder().username("jlong").password("pw").roles("USER").build());
  //}

   //@Bean
   //MapReactiveUserDetailsService authentication() {
   // return new MapReactiveUserDetailsService(
   //         User.builder()
   //                 .username("kostas")
   //                 .password("")
   //                 .roles("GENERAL_ADMIN")
   //                 .build());
   //}

  // @Bean
  // public PayloadSocketAcceptorInterceptor rsocketInterceptor(RSocketSecurity pattern) {
  //    rsocket.authorizePayload(authorize -> {
  //        authorize
  //                // must have ROLE_SETUP to make connection
  //                //.setup().hasRole("SETUP")
  //                // must have ROLE_ADMIN for routes starting with "taxis."
  //                //.route("taxis*").hasRole("ADMIN")
  //                // any other request must be authenticated for
  //                .anyRequest().authenticated();
  //    })
  //    return pattern
  //            .jwt(jwtSpec -> jwtSpec.authenticationManager(authenticationManager))
  //            .build();
  // }
}
