package com.droukos.aedservice.environment.dto.client.aed_event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter @ToString
@AllArgsConstructor
@NoArgsConstructor
public class AedEventDtoSearch {
    private Integer occurrenceType;
    private Integer status;
}
