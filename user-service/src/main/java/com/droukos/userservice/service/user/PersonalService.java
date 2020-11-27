package com.droukos.userservice.service.user;

import com.droukos.userservice.environment.dto.client.user.UpdateUserPersonal;
import com.droukos.userservice.model.factories.user.res.UserFactoryPersonal;
import com.droukos.userservice.model.user.UserRes;
import com.droukos.userservice.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@AllArgsConstructor
public class PersonalService {

  private final UserRepository userRepository;

  public Mono<Boolean> updateUserPersonal(Tuple2<UpdateUserPersonal, UserRes> tuple2) {

    UserRes updatedUser = UserFactoryPersonal.updatePersonalInfo(tuple2.getT2(), tuple2.getT1());
    return userRepository.save(updatedUser).then(Mono.just(true));
  }
}
