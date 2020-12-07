package com.droukos.aedservice.model.factories.aed_event;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoCreate;
import com.droukos.aedservice.model.aed_event.AedEvent;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.droukos.aedservice.environment.enums.AedEventStatus.PENDING;

public class AedEventFactoryCreate {

    private AedEventFactoryCreate(){}

    public static AedEvent eventCreate(AedEventDtoCreate aedEventDtoCreate){
        return new AedEvent(null,
                aedEventDtoCreate.getUsername().toLowerCase(),
                aedEventDtoCreate.getUsername(),
                aedEventDtoCreate.getOccurrenceType(),
                new GeoJsonPoint(aedEventDtoCreate.getMapX(), aedEventDtoCreate.getMapY()),
                aedEventDtoCreate.getAddress(),
                Objects.nonNull(aedEventDtoCreate.getComment())? aedEventDtoCreate.getComment(): "",
                null,
                null,
                PENDING.getStatus(),
                LocalDateTime.now(),
                null,
                null,
                null,
                null,
                null
        );
    }
}
