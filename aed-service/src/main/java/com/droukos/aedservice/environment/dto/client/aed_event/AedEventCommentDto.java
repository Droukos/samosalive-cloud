package com.droukos.aedservice.environment.dto.client.aed_event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AedEventCommentDto {
    private String eventId;
    private String comment;
}
