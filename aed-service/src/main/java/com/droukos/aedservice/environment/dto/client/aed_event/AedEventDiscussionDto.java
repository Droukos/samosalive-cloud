package com.droukos.aedservice.environment.dto.client.aed_event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AedEventDiscussionDto {
    private String eventId;
    private long pageOffset;
}
