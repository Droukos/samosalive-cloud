package com.droukos.userservice.service.user;

import static com.droukos.userservice.util.ValidatorUtil.validate;

import com.droukos.userservice.environment.constants.StatusCodes;
import com.droukos.userservice.environment.dto.client.user.UpdateAvailability;
import com.droukos.userservice.environment.dto.server.ApiResponse;
import com.droukos.userservice.model.user.UserRes;
import com.droukos.userservice.repo.UserRepository;
import com.droukos.userservice.service.validator.user.AvailabilityValidator;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
@Lazy
@RequiredArgsConstructor
public class AvailabilityService {

  @NonNull private final UserRepository userRepository;

  /*public Mono<UserRes> validateAvailability(User user) {
    Consumer<UpdateAvailability> prepareUpdate = updateAvailability -> user.getState()
        .setStatus(updateAvailability.getStatus());

    Function<UpdateAvailability, Mono<User>> process = updateAvailability -> {
      validate(updateAvailability, AvailabilityValidator.build());
      prepareUpdate.accept(updateAvailability);
      return Mono.just(user);
    };

    return request
        .bodyToMono(UpdateAvailability.class)
        .defaultIfEmpty(new UpdateAvailability())
        .flatMap(process);
  }

  public Mono<ServerResponse> updateAvailability(User user) {
    Function<User, Mono<ServerResponse>> result = savedUser -> ok().body(BodyInserters.fromValue(
        new ApiResponse(StatusCodes.OK, "Availability Updated", "user.availability.updated")));

    return userRepository.save(user)
        .flatMap(result);
  }
*/
}
