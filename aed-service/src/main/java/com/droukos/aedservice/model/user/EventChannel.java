package com.droukos.aedservice.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventChannel {
    private boolean isRescuer;

    public static EventChannel userAsRescuer() {
        return new EventChannel(true);
    }
}
