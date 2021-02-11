package com.droukos.userservice.service.user;

import com.droukos.userservice.environment.dto.RequesterAccessTokenData;
import com.droukos.userservice.environment.dto.client.user.UpdateUserPersonal;
import com.droukos.userservice.environment.dto.client.user.UpdateUserPrivacy;
import com.droukos.userservice.environment.dto.client.user.UserIdDto;
import com.droukos.userservice.environment.dto.server.user.RequestedPrivacySets;
import com.droukos.userservice.model.user.UserRes;
import com.droukos.userservice.model.user.privacy.PrivacySettingMap;
import com.droukos.userservice.repo.UserRepository;
import com.droukos.userservice.util.RolesUtil;
import com.droukos.userservice.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Map;
import java.util.function.Function;

import static com.droukos.userservice.environment.security.HttpExceptionFactory.badRequest;

@Service
@AllArgsConstructor
public class PrivacyService {

  private final UserRepository userRepository;

  public Mono<Boolean> saveToMongoDb(UserRes user) {
    return userRepository.save(user).then(Mono.just(true));
  }

  public Mono<UserIdDto> checkRequesterUserIdOrRole(Tuple2<UserIdDto, SecurityContext> tuple2) {

    RequesterAccessTokenData requesterAccessTokenData = SecurityUtil.getRequesterData(tuple2.getT2());
    String userid = tuple2.getT1().getUserid();
    Function<String, Mono<UserIdDto>> checkAdminRole = updateUserPersonal1 ->
            RolesUtil.hasAnyAdminRole(requesterAccessTokenData.getRoles())
                    ? Mono.just(tuple2.getT1()) : Mono.error(badRequest());

    return (!requesterAccessTokenData.getUserId().equals(userid))
            ? checkAdminRole.apply(userid)
            : Mono.just(tuple2.getT1());
  }

}