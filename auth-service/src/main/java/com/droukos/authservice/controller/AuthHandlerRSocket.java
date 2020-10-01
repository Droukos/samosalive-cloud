package com.droukos.authservice.controller;

import com.droukos.authservice.environment.dto.client.auth.SignupInfo;
import com.droukos.authservice.service.auth.AuthServices;
import com.droukos.authservice.service.auth.EmailService;
import com.droukos.authservice.service.auth.SignUpService;
import com.droukos.authservice.service.validator.auth.ValidatorFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandlerRSocket {

  @NonNull private final SignUpService signUpService;
  @NonNull private final EmailService emailService;
  @NonNull private final AuthServices authServices;

  @MessageMapping("auth.signup")
  public Mono<Boolean> signup(SignupInfo signupInfo) {

    return signUpService
        .buildUser(signupInfo)
        .doOnNext(ValidatorFactory::validateSignedUpUser)
        .doOnNext(signUpService::encodeUserPassword)
        .flatMap(signUpService::signedUpUser);
  }

  public Mono<Boolean> checkUsername(ServerRequest request) {

    return authServices
        .createUserDto(request)
        .flatMap(signUpService::checkUsernameUniqueness)
        .flatMap(signUpService::notFoundUsername);
  }

  public Mono<Boolean> checkEmail(ServerRequest request) {

    return authServices
        .createUserDto(request)
        .flatMap(signUpService::checkEmailUniqueness)
        .flatMap(signUpService::notFoundEmail);
  }

  public Mono<Boolean> emailChange(ServerRequest request) {

    return authServices
        .getUserByPathVarId(request)
        .flatMap(authServices::createUpdateEmailDto)
        .doOnNext(emailService::setEncodedPassFromDbToDto)
        .doOnNext(emailService::setEmailFromDbToDto)
        .flatMap(emailService::validateEmailChange)
        .doOnNext(emailService::setNewEmailToUser)
        .flatMap(emailService::changeUserOnDb);
  }
}
