package com.droukos.aedservice.model.factories.aed_event;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoCreate;
import com.droukos.aedservice.model.aed_event.AedEvent;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import static com.droukos.aedservice.environment.enums.AedEventStatus.PENDING;

public class AedEventFactoryCreate {

    private AedEventFactoryCreate(){}

    public static Mono<AedEvent> aedEventCreateMono(AedEventDtoCreate dto) {
        return Mono.just(aedEventCreate(dto));
    }

    public static AedEvent aedEventCreate(AedEventDtoCreate aedEventDtoCreate){
        return new AedEvent(null,
                aedEventDtoCreate.getUsername().toLowerCase(),
                aedEventDtoCreate.getUsername(),
                aedEventDtoCreate.getOccurrenceType(),
                new GeoJsonPoint(aedEventDtoCreate.getMapLat(), aedEventDtoCreate.getMapLon()),
                null,
                aedEventDtoCreate.getAddress(),
                Objects.nonNull(aedEventDtoCreate.getComment())? aedEventDtoCreate.getComment(): "",
                null,
                aedEventDtoCreate.getPhone(),
                PENDING.getStatus(),
                LocalDateTime.now(),
                null,
                null,
                null,
                aedEventDtoCreate.getCallee(),
                new ArrayList<>(),
                0
        );
    }
}
