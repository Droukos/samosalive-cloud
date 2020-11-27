package com.droukos.userservice.controller;

import com.droukos.userservice.environment.dto.client.user.*;
import com.droukos.userservice.environment.dto.server.user.RequestedPreviewUser;
import com.droukos.userservice.environment.dto.server.user.RequestedPrivacySets;
import com.droukos.userservice.environment.dto.server.user.RequestedUserInfo;
import com.droukos.userservice.model.factories.user.res.UserFactoryAvailability;
import com.droukos.userservice.model.factories.user.res.UserFactoryPrivacy;
import com.droukos.userservice.service.user.*;
import com.droukos.userservice.service.validator.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@Log4j2
@AllArgsConstructor
public class UserController {

  private final UserServices userServices;
  private final UserInfoService userInfoService;
  private final PrivacyService privacyService;
  private final PersonalService personalService;
  private final AvailabilityService availabilityService;

  @MessageMapping("user.filter.by.id")
  public Mono<RequestedUserInfo> getUserFilteredById(UserIdDto userIdDto) {

    return userServices
        .fetchUserById(userIdDto.getUserid())
        .zipWith(ReactiveSecurityContextHolder.getContext())
        .flatMap(userInfoService::getRequestedUsersFilterOnPrivSettings)
        .flatMap(RequestedUserInfo::buildMono);
  }

  @MessageMapping("user.get.username.like")
  public Flux<String> getUsernameLike(UsernameDto usernameDto) {

    return Flux.just(usernameDto.getUsername())
        .flatMap(ValidatorFactory::validateUsername)
        .flatMap(userInfoService::fetchUsernamesLike);
  }

  @MessageMapping("user.get.privacy.sets")
  public Mono<RequestedPrivacySets> getUserPrivacySets(UserIdDto userIdDto) {

    return Mono.just(userIdDto)
            .zipWith(ReactiveSecurityContextHolder.getContext())
            .flatMap(privacyService::checkRequesterUserIdOrRole)
            .flatMap(userServices::fetchUserById)
            .flatMap(RequestedPrivacySets::buildMono);
  }

  @MessageMapping("user.get.preview")
  public Flux<RequestedPreviewUser> getUsersPreview(PreviewUsernameDto previewUsernameDto) {

    return Flux.just(previewUsernameDto.getUsername())
        .flatMap(ValidatorFactory::validateUsername)
        .flatMap(userInfoService::fetchPreviewUsers)
        .flatMap(userInfoService::getRequestedUserFiltersOnPrivacySettings)
        .flatMap(RequestedPreviewUser::buildMono);
  }

  @MessageMapping("user.put.personal.info")
  public Mono<Boolean> putUserInfoPersonal(
      UpdateUserPersonal updateUserPersonal) {

    return Mono.just(updateUserPersonal)
        .doOnNext(ValidatorFactory::validateUserPersonal)
        .zipWith(ReactiveSecurityContextHolder.getContext())
        .flatMap(userInfoService::checkRequesterUserIdOrRole)
        .zipWith(userServices.fetchUserById(updateUserPersonal.getUserid()))
        .flatMap(personalService::updateUserPersonal);
  }

  @MessageMapping("user.put.privacy.info")
  public Mono<Boolean> putUserInfoPrivacySets(UpdateUserPrivacy updateUserPrivacy) {

    return Mono.just(updateUserPrivacy)
        .doOnNext(ValidatorFactory::validateUserPrivacy)
        .zipWith(userServices.fetchUserById(updateUserPrivacy.getUserid()))
        .flatMap(UserFactoryPrivacy::buildMonoUpdatePrivacy)
        .flatMap(privacyService::saveToMongoDb)
        .then(Mono.just(true));
  }

  @MessageMapping("user.put.availability.state.{id}")
  public Mono<Void> putStateAvailability(
      UpdateAvailability updateAvailability, @DestinationVariable("id") String userId) {

    return Mono.just(updateAvailability)
        .doOnNext(ValidatorFactory::validateUserAvailability)
        .zipWith(userServices.fetchUserById(userId))
        .flatMap(UserFactoryAvailability::buildMonoUserOnAvailbilityUpdate)
        .flatMap(availabilityService::saveToMongoDb)
        .then(Mono.empty());
  }
}
