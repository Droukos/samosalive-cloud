package com.droukos.userservice.service.user;

import com.droukos.userservice.model.user.UserRes;
import com.droukos.userservice.repo.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Lazy
@RequiredArgsConstructor
public class PrivacyService {

  @NonNull private final UserRepository userRepository;

  public Mono<Boolean> saveToMongoDb(UserRes user) {
    return userRepository.save(user).then(Mono.just(true));
  }
}
