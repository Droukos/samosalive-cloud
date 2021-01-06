package com.droukos.aedservice.controller.aed_event;

import com.droukos.aedservice.environment.dto.client.aed_event.*;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.CloseAedEvent;
import com.droukos.aedservice.environment.dto.server.aed.aedEvent.RequestedPreviewAedEvent;
import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceInfoPreviewDto;
import com.droukos.aedservice.environment.dto.server.user.RequestedPreviewRescuer;
import com.droukos.aedservice.environment.dto.server.user.RequestedPreviewUser;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.model.aed_event.AedEventComment;
import com.droukos.aedservice.model.factories.aed_event.AedEventFactoryClose;
import com.droukos.aedservice.model.factories.aed_event.AedEventFactoryComments;
import com.droukos.aedservice.model.factories.aed_event.AedEventFactorySubRescuer;
import com.droukos.aedservice.service.aed_event.AedEventChannel;
import com.droukos.aedservice.service.aed_event.AedEventInfo;
import com.droukos.aedservice.service.aed_event.channel.AedEventDeviceChannel;
import com.droukos.aedservice.service.aed_event.channel.AedEventDiscussionChannel;
import com.droukos.aedservice.service.aed_event.channel.AedEventRescuerChannel;
import com.droukos.aedservice.service.aed_event.channel.AedEventUserChannel;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class AedEventChannelController {
    private final AedEventChannel aedEventChannel;
    private final AedEventInfo aedEventInfo;
    private final AedEventDeviceChannel aedEventDeviceChannel;
    private final AedEventRescuerChannel aedEventRescuerChannel;
    private final AedEventDiscussionChannel aedEventDiscussionChannel;
    private final AedEventUserChannel aedEventUserChannel;

    @MessageMapping("aed.event.listen")
    public Flux<RequestedPreviewAedEvent> listenEvents() {
        return aedEventChannel.fetchPublishedAedEvents()
                .flatMap(RequestedPreviewAedEvent::buildMono);
    }

    @MessageMapping("aed.event.listen.sub")
    public Flux<AedEvent> listenSubEvent(AedEventDtoIdSearch aedEventDtoIdSearch) {
        return aedEventInfo.findEventById(aedEventDtoIdSearch.getId())
                .zipWith(ReactiveSecurityContextHolder.getContext())
                .flatMap(aedEventChannel::validateListenerForEvent)
                .flatMap(aedEventChannel::checkAndInsertUserSubOnDb)
                .flatMap(aedEventUserChannel::publishUserForSingleEvent)
                .flatMap(aedEventChannel::checkAndInsertAedEventSubOnDb)
                .flatMapMany(aedEventChannel::fetchPublishedAedEventsFromSingle);
    }

    @MessageMapping("aed.event.listen.sub.device")
    public Flux<AedDeviceInfoPreviewDto> listenSubEventAedDevice(AedEventDtoIdSearch aedEventDtoIdSearch) {
        return aedEventInfo.findEventById(aedEventDtoIdSearch.getId())
                .zipWith(ReactiveSecurityContextHolder.getContext())
                .flatMap(aedEventChannel::validateListenerForEventPostfix)
                .flatMapMany(aedEventDeviceChannel::fetchPublishedAedDeviceFromSingleEvent);
    }

    @MessageMapping("aed.event.listen.sub.user")
    public Flux<RequestedPreviewUser> listenSubEventUsers(AedEventDtoIdSearch aedEventDtoIdSearch) {
        return aedEventInfo.findEventById(aedEventDtoIdSearch.getId())
                .zipWith(ReactiveSecurityContextHolder.getContext())
                .flatMap(aedEventChannel::validateListenerForEventPostfix)
                .flatMapMany(aedEventUserChannel::fetchPublishedUsersForSingleEvent);
    }

    @MessageMapping("aed.event.listen.sub.rescuer")
    public Flux<RequestedPreviewRescuer> listenSubEventRescuer(AedEventDtoIdSearch aedEventDtoIdSearch) {
        return aedEventInfo.findEventById(aedEventDtoIdSearch.getId())
                .zipWith(ReactiveSecurityContextHolder.getContext())
                .flatMap(aedEventChannel::validateListenerForEventPostfix)
                .flatMapMany(aedEventRescuerChannel::fetchPublishedRescuerFromSingleEvent);
    }

    @MessageMapping("aed.event.listen.sub.discussion")
    public Flux<AedEventComment> listenSubEventComments(AedEventDtoIdSearch aedEventDtoIdSearch) {
        return aedEventInfo.findEventById(aedEventDtoIdSearch.getId())
                .zipWith(ReactiveSecurityContextHolder.getContext())
                .flatMap(aedEventChannel::validateListenerForEventPostfix)
                .flatMapMany(aedEventDiscussionChannel::fetchPublishedCommentsForSingleEvent);
    }

    @MessageMapping("aed.event.sub.fetch.discussion")
    public Flux<AedEventComment> fetchSubEventComments(AedEventDiscussionDto dto) {
        return aedEventInfo.findEventById(dto.getEventId())
                .zipWith(ReactiveSecurityContextHolder.getContext())
                .flatMap(aedEventChannel::validateUserForDiscussion)
                .then(Mono.just(dto))
                .flatMapMany(aedEventChannel::fetchEventComments);
    }

    @MessageMapping("aed.event.sub.post.comment")
    public Mono<Boolean> eventPostComment(AedEventCommentDto dto) {

        return aedEventDiscussionChannel.validateComment(dto)
                .then(aedEventInfo.findEventById(dto.getEventId()))
                .flatMap(AedEventFactoryComments::add1CommNumOnEventMono)
                .flatMap(aedEvent ->
                        Mono.zip(
                                Mono.just(aedEvent),
                                Mono.just(dto),
                                ReactiveSecurityContextHolder.getContext())
                )
                .flatMap(aedEventDiscussionChannel::validateUserBelongsToChannel)
                .flatMap(aedEventDiscussionChannel::saveEvent)
                .flatMap(aedEventDiscussionChannel::saveOnEventDiscussion)
                .flatMap(aedEventDiscussionChannel::publishOnEventDiscussion)
                .then(Mono.just(true));
    }

    @MessageMapping("aed.event.subRescuer")
    public Mono<AedEvent> setEventRescuer(AedEventDtoRescuerSub aedEventDtoRescuerSub) {
        return ReactiveSecurityContextHolder.getContext()
                .zipWith(Mono.just(aedEventDtoRescuerSub))
                .flatMap(aedEventInfo::fetchRescuerFromDtoOrContext)
                .flatMap(aedEventInfo::findEventDeviceRescuerAggregation)
                .flatMap(aedEventInfo::validateAedDeviceForRescuing)
                .flatMap(AedEventFactorySubRescuer::subAedRescuerAndDeviceMono)
                .flatMap(aedEventInfo::saveUser)
                .flatMap(aedEventRescuerChannel::publishRescuerOnRedisEventSubChannel)
                .flatMap(aedEventInfo::saveAedDevice)
                .flatMap(aedEventDeviceChannel::publishDeviceOnRedisEventSubChannel)
                .flatMap(aedEventInfo::saveAedEvent)
                .flatMap(aedEventChannel::publishEventOnRedisChannel)
                .flatMap(aedEventChannel::publishEventOnRedisSingleChannel);
    }

    @MessageMapping("aed.event.close")
    public Mono<CloseAedEvent> closeAedEvent(AedEventDtoClose aedEventDtoClose) {
        return aedEventInfo.findEventById(aedEventDtoClose.getId())
                .zipWith(ReactiveSecurityContextHolder.getContext())
                .flatMap(aedEventInfo::validateIfEventIsClosable)
                .zipWith(Mono.just(aedEventDtoClose))
                .flatMap(aedEventInfo::fetchAedDeviceForClosingEvent)
                .flatMap(AedEventFactoryClose::closeAedEvent)
                .flatMap(aedEventInfo::saveAedDevice)
                .flatMap(aedEventDeviceChannel::publishDeviceOnRedisEventSubChannel)
                .flatMap(aedEventInfo::saveAedEvent)
                .flatMap(aedEventChannel::publishEventOnRedisSingleChannel)
                .flatMap(aedEventChannel::removeUsersChannelSubFromDb)
                .then(Mono.empty());
    }
}
