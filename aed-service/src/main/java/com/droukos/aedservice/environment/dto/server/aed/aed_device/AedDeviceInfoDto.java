package com.droukos.aedservice.environment.dto.server.aed.aed_device;

import com.droukos.aedservice.model.aed_device.AedDevice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.geo.GeoJson;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AedDeviceInfoDto {
    private String id;
    private String nickname;
    private String modelName;
    private String description;

    private LocalDateTime added;
    private String addedBy;
    private LocalDateTime created;
    private Integer status;
    private String statusDescription;

    //TODO check on the raw parameterized class 'GeoJson'
    private GeoJson defaultMap;
    private String picUrl;
    private String addressPicUrl;
    private String addr;

    //TODO check on the raw parameterized class 'GeoJson'
    private GeoJson onMap;
    private String onEventId;
    private String onUserId;
    private LocalDateTime takenOn;
    private long onEstimatedFinish;

    public static Mono<AedDeviceInfoDto> buildMono(AedDevice aedDevice) {
        return Mono.just(build(aedDevice));
    }

    public static AedDeviceInfoDto build(AedDevice aedDevice) {
        return new AedDeviceInfoDto(
                aedDevice.getId(),
                aedDevice.getNickname(),
                aedDevice.getModelName(),
                aedDevice.getDescr(),
                aedDevice.getAdded(),
                aedDevice.getAddedBy(),
                aedDevice.getCreated(),
                aedDevice.getStatus(),
                aedDevice.getStatusDescr(),
                aedDevice.getDefMap(),
                aedDevice.getPicUrl(),
                aedDevice.getAddrPicUrl(),
                aedDevice.getAddr(),
                aedDevice.getOnMap(),
                aedDevice.getOnEvId(),
                aedDevice.getOnUId(),
                aedDevice.getTakenOn(),
                aedDevice.getOnEstFin()
        );
    }
}
