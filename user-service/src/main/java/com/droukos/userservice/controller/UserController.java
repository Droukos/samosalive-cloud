package com.droukos.userservice.controller;

import com.droukos.userservice.environment.dto.client.user.PreviewUsernameDto;
import com.droukos.userservice.environment.dto.client.user.UpdateAvailability;
import com.droukos.userservice.environment.dto.client.user.UpdateUserPersonal;
import com.droukos.userservice.environment.dto.client.user.UpdateUserPrivacy;
import com.droukos.userservice.environment.dto.server.user.RequestedPreviewUser;
import com.droukos.userservice.environment.dto.server.user.RequestedUserInfo;
import com.droukos.userservice.model.factories.user.res.UserFactoryAvailability;
import com.droukos.userservice.model.factories.user.res.UserFactoryPrivacy;
import com.droukos.userservice.service.user.*;
import com.droukos.userservice.service.validator.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
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
  private final AvatarService avatarService;
  private final AvailabilityService availabilityService;

  @MessageMapping("user.filter.by.id.{id}")
  public Mono<RequestedUserInfo> getUserFilteredById(@DestinationVariable("id") String userId) {

    return userServices
        .fetchUserById(userId)
        .zipWith(ReactiveSecurityContextHolder.getContext())
        .flatMap(userInfoService::getRequestedUsersFilterOnPrivSettings)
        .flatMap(RequestedUserInfo::buildMono);
  }

  @MessageMapping("user.get.username.like.{username}")
  public Flux<String> getUsernameLike(@DestinationVariable("username") String username) {

    return Flux.just(username)
        .filter(ValidatorFactory::validateUsername)
        .flatMap(userInfoService::fetchUsernamesLike);
  }

  @MessageMapping("user.get.preview")
  public Flux<RequestedPreviewUser> getUsersPreview(PreviewUsernameDto previewUsernameDto) {

    return Flux.just(previewUsernameDto.getUsername())
        .filter(ValidatorFactory::validateUsername)
        .flatMap(userInfoService::fetchPreviewUsers)
        .flatMap(userInfoService::getRequestedUserFiltersOnPrivacySettings)
        .flatMap(RequestedPreviewUser::buildMono);
  }

  @MessageMapping("user.put.personal.info.{id}")
   public Mono<Boolean> putUserInfoPersonal
          (UpdateUserPersonal updateUserPersonal, @DestinationVariable("id") String userId) {

    return Mono.just(updateUserPersonal)
            .doOnNext(ValidatorFactory::validateUserPersonal)
            .zipWith(userServices.fetchUserById(userId))
            .flatMap(personalService::updateUserPersonal);
   }

   //public Mono<ServerResponse> putUserInfoAvatar(ServerRequest request) {
   // var avatarService = userServices.getAvatarService().setRequest(request);
//
   // return userServices.getUserByPathVarId(request)
   //         .flatMap(avatarService::fetchAvatarFromMPData)
   //         .flatMap(avatarService::validateAvatar)
   //         .flatMap(avatarService::updateUserAvatarAndResponse);
   //}

    @MessageMapping("user.put.privacy.info.{id}")
    public Mono<Boolean> putUserInfoPrivacySets
            (UpdateUserPrivacy updateUserPrivacy, @DestinationVariable("id") String userId) {

        return Mono.just(updateUserPrivacy)
                .doOnNext(ValidatorFactory::validateUserPrivacy)
                .zipWith(userServices.fetchUserById(userId))
                .flatMap(UserFactoryPrivacy::buildMonoUpdatePrivacy)
                .flatMap(privacyService::saveToMongoDb);
    }

    @MessageMapping("user.put.availability.state.{id}")
   public Mono<Boolean> putStateAvailability
           (UpdateAvailability updateAvailability, @DestinationVariable("id") String userId) {

      return Mono.just(updateAvailability)
              .doOnNext(ValidatorFactory::validateUserAvailability)
              .zipWith(userServices.fetchUserById(userId))
              .flatMap(UserFactoryAvailability::buildMonoUserOnAvailbilityUpdate)
              .flatMap(availabilityService::saveToMongoDb);
   }
}
