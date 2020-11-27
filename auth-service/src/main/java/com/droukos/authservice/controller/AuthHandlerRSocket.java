package com.droukos.authservice.controller;

import com.droukos.authservice.environment.dto.client.auth.CheckUniqueness;
import com.droukos.authservice.environment.dto.client.auth.SignupInfo;
import com.droukos.authservice.service.auth.SignUpService;
import com.droukos.authservice.service.validator.auth.ValidatorFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class AuthHandlerRSocket {

  @NonNull private final SignUpService signUpService;

  @MessageMapping("auth.signup")
  public Mono<Boolean> signup(SignupInfo signupInfo) {

    return Mono.just(signupInfo)
            .doOnNext(ValidatorFactory::validateSignedUpUser)
            .flatMap(signUpService::checkUsernameEmailUniqueness)
            .flatMap(signUpService::buildUser)
            .flatMap(signUpService::signedUpUser);
  }

  @MessageMapping("auth.username.check")
  public Mono<Boolean> checkUsername(CheckUniqueness checkUniqueness) {

    return signUpService.checkUsernameUniqueness(checkUniqueness);
  }

  @MessageMapping("auth.email.check")
  public Mono<Boolean> checkEmail(CheckUniqueness checkUniqueness) {

    return signUpService.checkEmailUniqueness(checkUniqueness);
  }

  @MessageMapping("auth.email.change")
  public Mono<Boolean> emailChange() {

    return Mono.just(true);
  }
}
