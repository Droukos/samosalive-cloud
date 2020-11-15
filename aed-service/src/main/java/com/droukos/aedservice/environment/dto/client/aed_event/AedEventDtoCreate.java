package com.droukos.aedservice.environment.dto.client.aed_event;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class AedEventDtoCreate {
    private String userid;
    private String username;
    private Integer occurrenceType;
    private String address;
    private String comment;
    private Integer status;
    private String requestedTime;
}
