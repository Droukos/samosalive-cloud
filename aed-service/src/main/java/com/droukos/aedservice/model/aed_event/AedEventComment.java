package com.droukos.aedservice.model.aed_event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AedEventComment {
    private String username;
    private String message;
    private LocalDateTime posted;
}
