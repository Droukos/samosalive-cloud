package com.droukos.aedservice.environment.dto.client.aed_event;

import lombok.*;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class AedEventDtoCreate {
    private String userid;
    private String username;
    private String occurrenceType;
    private String address;
    private String comment;
    private String status;
}
