package com.droukos.userservice.service.user;

import static com.droukos.userservice.util.ValidatorUtil.validate;

import com.droukos.userservice.environment.constants.FieldNames;
import com.droukos.userservice.environment.constants.StatusCodes;
import com.droukos.userservice.environment.dto.client.user.UpdateUserPrivacy;
import com.droukos.userservice.environment.dto.server.ApiResponse;
import com.droukos.userservice.model.factories.user.res.UserFactoryPrivacy;
import com.droukos.userservice.model.user.UserRes;
import com.droukos.userservice.model.user.privacy.PrivacySetting;
import com.droukos.userservice.model.user.privacy.PrivacySettingMap;
import com.droukos.userservice.repo.UserRepository;
import com.droukos.userservice.service.validator.user.PrivacySettingsValidator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@Lazy
@RequiredArgsConstructor
public class PrivacyService {

  @NonNull private final UserRepository userRepository;

  public Mono<Boolean> saveToMongoDb(UserRes user) {
      return userRepository.save(user)
              .then(Mono.just(true));
  }
}
