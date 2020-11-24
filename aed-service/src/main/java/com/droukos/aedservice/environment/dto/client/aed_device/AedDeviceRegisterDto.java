package com.droukos.aedservice.environment.dto.client.aed_device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJson;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AedDeviceRegisterDto {
    private String uniqueNickname;
    private String modelName;
    private String description;
    private double defaultMapX;
    private double defaultMapY;
    private String address;
}
