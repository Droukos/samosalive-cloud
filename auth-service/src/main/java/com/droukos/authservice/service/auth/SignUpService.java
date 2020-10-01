package com.droukos.authservice.service.auth;

import com.droukos.authservice.environment.dto.client.auth.SignupInfo;
import com.droukos.authservice.environment.dto.server.responses.BadResponses;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.repo.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.droukos.authservice.util.UserGenUtil.buildUserFromSignupInfoDto;

@Service
@RequiredArgsConstructor
public class SignUpService {

  @NonNull private final UserRepository userRepository;
  @NonNull private final BCryptPasswordEncoder passwordEncoder;

  public Mono<UserRes> buildUser(SignupInfo signupInfo) {
     return Mono.just(buildUserFromSignupInfoDto(passwordEncoder, signupInfo));
  }

  public void encodeUserPassword(UserRes user) {
      user.setPass(passwordEncoder.encode(user.getPass()));
  }

  public Mono<Boolean> signedUpUser(UserRes user) {
    return userRepository.save(user).flatMap(savedUser -> Mono.just(true));
  }

  public Mono<UserRes> checkUsernameUniqueness(UserRes user) {
    return userRepository
        .findFirstByUser(user.getUser().toLowerCase())
        .filter(duplicatedUser -> duplicatedUser.getId() != null)
        .flatMap(duplicatedUser -> new BadResponses().usernameTaken())
        .switchIfEmpty(Mono.just(user));
  }

  public Mono<UserRes> checkEmailUniqueness(UserRes user) {
    return userRepository
        .findFirstByEmail(user.getEmail().toLowerCase())
        .filter(duplicatedUser -> duplicatedUser.getId() != null)
        .flatMap(duplicatedUser -> new BadResponses().emailTaken())
        .switchIfEmpty(Mono.just(user));
  }

   public Mono<Boolean> notFoundUsername(UserRes user) {
    return Mono.just(true);
   }

   public Mono<Boolean> notFoundEmail(UserRes user) {
    return Mono.just(true);
   }
}
