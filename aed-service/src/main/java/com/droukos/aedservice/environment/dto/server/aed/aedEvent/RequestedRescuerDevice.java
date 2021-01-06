package com.droukos.aedservice.environment.dto.server.aed.aedEvent;

import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceInfoPreviewDto;
import com.droukos.aedservice.environment.dto.server.user.RequestedPreviewRescuer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestedRescuerDevice {
    private AedDeviceInfoPreviewDto devicePreview;
    private RequestedPreviewRescuer rescuerPreview;
}
