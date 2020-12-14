package com.droukos.aedservice.model.factories.user;

import com.droukos.aedservice.model.user.ChannelSubs;
import com.droukos.aedservice.model.user.UserRes;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class UserAedEventChannelSub {
    private UserAedEventChannelSub() {
    }

    public static Mono<UserRes> buildUserByRemovingSubMono(UserRes user, String channel) {
        return Mono.just(buildUserByRemovingSub(user, channel));
    }

    public static UserRes buildUserByRemovingSub(UserRes user, String channel) {
        return (user.getChannelSubs().getAedEvSubs() == null)
                ? user
                : new UserRes(
                        user.getId(),
                        user.getUser(),
                        user.getUserC(),
                        user.getPass(),
                        user.getPassC(),
                        user.getEmail(),
                        user.getEmailC(),
                        user.getAllRoles(),
                        user.getPrsn(),
                        user.getPrivy(),
                        user.getSys(),
                        new ChannelSubs(
                            user.getChannelSubs().getAedEvSubs()
                                    .stream()
                                    .filter(subChannel -> !subChannel.equals(channel))
                                    .collect(Collectors.toList())
                                ,
                                user.getChannelSubs().getAedPrSubs()
                        ),
                        user.getAppState()
        );
    }

    public static Mono<UserRes> buildUserNewEventSubListMono(UserRes user, String channel) {
        return Mono.just(buildUserNewEventSubList(user, channel));
    }

    public static UserRes buildUserNewEventSubList(UserRes user, String channel) {
        return (user.getChannelSubs() == null || user.getChannelSubs().getAedEvSubs() == null)
                ? new UserRes(
                user.getId(),
                user.getUser(),
                user.getUserC(),
                user.getPass(),
                user.getPassC(),
                user.getEmail(),
                user.getEmailC(),
                user.getAllRoles(),
                user.getPrsn(),
                user.getPrivy(),
                user.getSys(),
                new ChannelSubs(
                        Collections.singletonList(channel),
                        user.getChannelSubs() != null
                                ? user.getChannelSubs().getAedPrSubs()
                                : null),
                user.getAppState()
        ) : new UserRes(
                user.getId(),
                user.getUser(),
                user.getUserC(),
                user.getPass(),
                user.getPassC(),
                user.getEmail(),
                user.getEmailC(),
                user.getAllRoles(),
                user.getPrsn(),
                user.getPrivy(),
                user.getSys(),
                new ChannelSubs(
                        Stream.concat(user.getChannelSubs().getAedEvSubs().stream(), Stream.of(channel))
                                .collect(Collectors.toList()),
                        user.getChannelSubs().getAedPrSubs()),
                user.getAppState()
        );
    }

}
