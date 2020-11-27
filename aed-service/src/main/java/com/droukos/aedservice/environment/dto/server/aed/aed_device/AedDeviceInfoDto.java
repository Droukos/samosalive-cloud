package com.droukos.aedservice.environment.dto.server.aed.aed_device;

import com.droukos.aedservice.model.aed_device.AedDevice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.geo.Point;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AedDeviceInfoDto {
    private String id;
    private String uniqueNickname;
    private String modelName;
    private String description;

    private LocalDateTime added;
    private String addedBy;
    private Integer status;
    private String statusDescription;

    private Point homePoint;
    private String picUrl;
    private String addressPicUrl;
    private String address;

    private Point onPoint;
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
                aedDevice.getUniqNickname(),
                aedDevice.getModelName(),
                aedDevice.getDesc(),
                aedDevice.getAdded(),
                aedDevice.getAddedBy(),
                aedDevice.getStatus(),
                aedDevice.getStatusDesc(),
                aedDevice.getHomeP(),
                aedDevice.getPicUrl(),
                aedDevice.getAddrPicUrl(),
                aedDevice.getAddr(),
                aedDevice.getOnP(),
                aedDevice.getOnEvId(),
                aedDevice.getOnUId(),
                aedDevice.getTakenOn(),
                aedDevice.getOnEstFin()
        );
    }
}
