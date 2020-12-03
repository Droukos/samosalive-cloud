package com.droukos.aedservice.environment.dto.client.aed_device.problems;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AedDeviceProblemRegisterDto {
    private String username;
    private String aedDeviceId;
    private String problemTitle;
    private String problemBody;
    private String address;
    private double y;
    private double x;
}
