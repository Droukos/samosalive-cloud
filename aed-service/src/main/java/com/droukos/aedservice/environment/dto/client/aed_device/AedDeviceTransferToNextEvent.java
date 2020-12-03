package com.droukos.aedservice.environment.dto.client.aed_device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AedDeviceTransferToNextEvent {
    private String currentEventId;
    private String nextEventId;
    private String aedDeviceId;
    private String nextUserId;
}
