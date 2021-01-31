package com.droukos.aedservice.model.factories.sub;

import com.droukos.aedservice.environment.enums.UserSubTypes;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.model.sub.UserSub;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;

public class UserSubAedFactory {
    private UserSubAedFactory() {}
    public static Mono<Tuple2<AedEvent, UserSub>> userSubsAedCreateZipMono(AedEvent aedEvent) {
        return Mono.zip(Mono.just(aedEvent),Mono.just(userSubAedCreate(aedEvent)));
    }
    public static UserSub userSubAedCreate(AedEvent aedEvent) {
        return new UserSub(
                null,
                aedEvent.getId(),
                UserSubTypes.AED_EVENT.getCode(),
                aedEvent.getId(),
                LocalDateTime.now()
        );
    }
}
