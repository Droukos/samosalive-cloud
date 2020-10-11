package com.droukos.userservice.service.user;

import com.droukos.userservice.model.user.UserRes;
import com.droukos.userservice.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.droukos.userservice.environment.security.HttpExceptionFactory.badRequest;

@Service
@AllArgsConstructor
public class UserServices {

  private final UserRepository userRepository;

  public Mono<UserRes> fetchUserById(String userId) {
    return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(badRequest("User does not exists")));
  }
}
