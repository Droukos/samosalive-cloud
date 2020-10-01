package com.droukos.authservice.service.auth;

import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.repo.UserRepository;
import com.droukos.authservice.service.validator.auth.ValidatorFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EmailService {

  @NonNull private final UserRepository userRepository;

  public void setNewEmailToUser(UserRes user) {
    user.setEmail(user.getUpdateEmail().getNewEmail().toLowerCase());
    user.setEmailC(user.getUpdateEmail().getNewEmail());
  }

  public void setEmailFromDbToDto(UserRes user) {
    user.getUpdateEmail().setEmail(user.getEmail());
  }

  public void setEncodedPassFromDbToDto(UserRes user) {
    user.getUpdateEmail().setPassOnDB(user.getPass());
  }

  public Mono<UserRes> validateEmailChange(UserRes user) {

    return Mono.just(user.getUpdateEmail())
        .doOnNext(ValidatorFactory::validateEmailChange)
        .then(Mono.just(user));
  }

  public Mono<Boolean> changeUserOnDb(UserRes user) {
    return userRepository.save(user).flatMap(savedUser -> Mono.just(true));
  }
}
