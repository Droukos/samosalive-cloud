package com.droukos.aedservice.environment.dto.server.aed.aed_device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AedDeviceCdnToken {
    private String id;
    private String token;

    public static AedDeviceCdnToken buildTokenForNewDev(String userid) {
        return new AedDeviceCdnToken(
                userid + ":aedNewDev",
                UUID.randomUUID().toString()
        );
    }

    public static AedDeviceCdnToken buildTokenForDevPic(String userid) {
        return new AedDeviceCdnToken(
                userid + ":aedDevPic",
                UUID.randomUUID().toString()
        );
    }
    public static AedDeviceCdnToken buildTokenForAddrPic(String userid) {
        return new AedDeviceCdnToken(
                userid + ":aedAddrPic",
                UUID.randomUUID().toString()
        );
    }
}
