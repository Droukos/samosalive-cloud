package com.droukos.authservice.service.auth;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.environment.dto.client.auth.UpdatePassword;
import com.droukos.authservice.environment.security.TokenService;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.repo.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;

import static com.droukos.authservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@RequiredArgsConstructor
public class PasswordService {

  @NonNull private final BCryptPasswordEncoder bCryptPasswordEncoder;
  @NonNull private final TokenService tokenService;
  @NonNull private UserRepository userRepository;

  public Mono<Tuple4<UpdatePassword, UserRes, NewAccTokenData, NewRefTokenData>>
      setNewAccessTokenIdToRedis(
          Tuple4<UpdatePassword, UserRes, NewAccTokenData, NewRefTokenData> tuple4) {

    return tokenService.redisSetUserToken(tuple4.getT2(), tuple4.getT3()).then(Mono.just(tuple4));
  }

  public Mono<Tuple3<UserRes, NewAccTokenData, NewRefTokenData>> saveChangedUserPassword(
      Tuple4<UpdatePassword, UserRes, NewAccTokenData, NewRefTokenData> tuple4) {

    String newEncryptedPassword = bCryptPasswordEncoder.encode(tuple4.getT1().getPassNew());
    return userRepository
        .save(UserRes.passwordUpdate(newEncryptedPassword, tuple4.getT2()))
        .then(Mono.zip(
                Mono.just(tuple4.getT2()),
                Mono.just(tuple4.getT3()),
                Mono.just(tuple4.getT4()))
        );
  }

  public Mono<UserRes> saveUserTokenChangesOnDb(UserRes user) {
    return userRepository.save(user);
  }

  public Mono<Tuple2<UpdatePassword, UserRes>> validateOldPassword(
      Tuple2<UpdatePassword, UserRes> tuple2) {

    return bCryptPasswordEncoder.matches(tuple2.getT1().getPass(), tuple2.getT2().getPass())
        ? Mono.just(tuple2)
        : Mono.error(badRequest("Old Pass not match"));
  }
}
