package com.droukos.cdnservice.environment.services;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AedDeviceServices {
    PUT_AED_DEVICE_PICS("put_aed_device_pics/{id}");

    private final String url;

    public String getFullUrl() {
        return "api/cdn/" + this.getUrl();
    }

}
