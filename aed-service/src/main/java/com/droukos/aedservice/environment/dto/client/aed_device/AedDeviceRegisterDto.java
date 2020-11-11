package com.droukos.aedservice.environment.dto.client.aed_device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.geo.GeoJson;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AedDeviceRegisterDto {
    private String nickname;
    private String modelName;
    private String description;
    //TODO check on the raw parameterized class 'GeoJson'
    private GeoJson defaultMap;
    private String address;
}
