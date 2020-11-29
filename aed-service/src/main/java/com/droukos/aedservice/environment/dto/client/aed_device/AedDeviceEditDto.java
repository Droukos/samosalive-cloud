package com.droukos.aedservice.environment.dto.client.aed_device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AedDeviceEditDto {
    private String id;
    private String modelName;
    private String modelDescription;
    private String address;
    private double homePointX;
    private double homePointY;
}
