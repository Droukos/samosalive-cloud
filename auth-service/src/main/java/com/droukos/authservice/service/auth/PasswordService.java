package com.droukos.authservice.service.auth;

import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.service.validator.auth.ValidatorFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PasswordService {

  @NonNull private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public void addEncodedPasswordFromDbToDto(UserRes user) {
    user.getUpdatePassword().setPassOnDb(user.getPass());
  }

  public void encodeNewPasswordToUser(UserRes user) {
    user.setPass(bCryptPasswordEncoder.encode(user.getUpdatePassword().getPassNew()));
  }

  public Mono<UserRes> validateNewPassword(UserRes user) {

    return Mono.just(user.getUpdatePassword())
        .doOnNext(ValidatorFactory::validatePasswordChange)
        .then(Mono.just(user));
  }
}
