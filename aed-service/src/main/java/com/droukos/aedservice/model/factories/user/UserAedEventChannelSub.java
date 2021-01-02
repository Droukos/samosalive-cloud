package com.droukos.aedservice.model.factories.user;

import com.droukos.aedservice.model.user.ChannelSubs;
import com.droukos.aedservice.model.user.EventChannel;
import com.droukos.aedservice.model.user.UserRes;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class UserAedEventChannelSub {
    private UserAedEventChannelSub() {
    }

    public static Mono<UserRes> buildUserByRemovingSubMono(UserRes user, String channel) {
        return Mono.just(buildUserByRemovingSub(user, channel));
    }

    public static UserRes buildUserByRemovingSub(UserRes user, String channel) {

        user.getChannelSubs().getAedEvSubs().remove(channel);

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
                                user.getChannelSubs().getAedEvSubs(),
                                user.getChannelSubs().getAedPrSubs()),
                        user.getAppState()
        );
    }

    public static Mono<UserRes> buildUserNewEventSubListMono(UserRes user, String channel) {
        return Mono.just(buildUserNewEventSubList(user, channel));
    }
    public static Mono<UserRes> buildUserWithoutSysMono(UserRes user) {
        return Mono.just(buildUserWithoutSys(user));
    }
    public static UserRes buildUserWithoutSys(UserRes user) {
        return new UserRes(
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
                null,
                user.getChannelSubs(),
                user.getAppState()
        );
    }

    public static UserRes buildUserNewEventSubList(UserRes user, String channel) {
        Map<String, EventChannel> dbMap = user.getChannelSubs().getAedEvSubs();
        if(user.getChannelSubs() == null || dbMap == null) {
            dbMap = new HashMap<>();
        }
        dbMap.put(channel, new EventChannel(false));

        return new UserRes(
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
                        dbMap,
                        user.getChannelSubs() != null
                                ? user.getChannelSubs().getAedPrSubs()
                                : null),
                user.getAppState()
        );
    }
}
