package com.droukos.aedservice.model.factories;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoCreate;
import com.droukos.aedservice.model.aed_event.AedEvent;

import java.util.Objects;

public class AedEventFactoryCreate {

    private AedEventFactoryCreate(){}

    public static AedEvent eventCreate(AedEventDtoCreate aedEventDtoCreate){
        return new AedEvent(null,
                aedEventDtoCreate.getUsername().toLowerCase(),
                aedEventDtoCreate.getUsername(),
                aedEventDtoCreate.getOccurrenceType(),
                null,
                aedEventDtoCreate.getAddress(),
                Objects.nonNull(aedEventDtoCreate.getComment())? aedEventDtoCreate.getComment(): "",
                null,
                null,
                aedEventDtoCreate.getStatus(),
                aedEventDtoCreate.getRequestedTime(),
                null,
                null
        );
    }
}
