package com.droukos.userservice.service.user;

import static com.droukos.userservice.util.ValidatorUtil.validate;

import com.droukos.userservice.environment.dto.client.user.UpdateUserPersonal;
import com.droukos.userservice.model.factories.user.res.UserFactoryPersonal;
import com.droukos.userservice.model.user.UserRes;
import com.droukos.userservice.repo.UserRepository;
import com.droukos.userservice.service.validator.user.PersonalValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@Lazy
@RequiredArgsConstructor
public class PersonalService {

  @NonNull private final UserRepository userRepository;

  //public void validateUserPersonal(UpdateUserPersonal updateUserPersonal) {
    //Consumer<UpdateUserPersonal> prepareUpdate = updateUserPersonal -> {
    //  user.setName(updateUserPersonal.getName());
    //  user.setSurname(updateUserPersonal.getSur());
    //  user.setDescription(updateUserPersonal.getDesc());
    //  user.setCountryIso(updateUserPersonal.getCiso());
    //  user.setProvince(updateUserPersonal.getState());
    //  user.setCity(updateUserPersonal.getCity());
    //};
  //}

  public Mono<Boolean> updateUserPersonal(Tuple2<UserRes, UpdateUserPersonal> tuple2) {

    UserRes updatedUser = UserFactoryPersonal.updatePersonalInfo(tuple2.getT1(), tuple2.getT2());
    return userRepository.save(updatedUser)
            .then(Mono.just(true));
    //Function<User, Mono<ServerResponse>> result = savedUser -> ok().body(BodyInserters.fromValue(
    //    new ApiResponse(StatusCodes.OK, "User Info Updated", "user.personalinfo_updated")));
//
    //return userRepository.save(user)
    //    .flatMap(result);
  }
}
