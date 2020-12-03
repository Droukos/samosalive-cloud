package com.droukos.aedservice.environment.dto.server.aed.aed_device;

import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.util.GeoJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import reactor.core.publisher.Mono;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AedDeviceInfoPreviewDto {
    private String id;
    private String uniqueNickname;
    private String modelName;
    private String description;
    private int status;
    private String picUrl;
    private String address;
    @JsonDeserialize(using = GeoJsonDeserializer.class)
    private Point homePoint;
    private String onEventId;
    private String onUserId;

    public static Mono<AedDeviceInfoPreviewDto> buildMono(AedDevice aedDevice) {
        return Mono.just(build(aedDevice));
    }

    public static AedDeviceInfoPreviewDto build(AedDevice aedDevice) {
        return new AedDeviceInfoPreviewDto(
                aedDevice.getId(),
                aedDevice.getUniqNickname(),
                aedDevice.getModelName(),
                aedDevice.getDesc(),
                aedDevice.getStatus(),
                aedDevice.getPicUrl(),
                aedDevice.getAddr(),
                aedDevice.getHomeP(),
                aedDevice.getOnEvId(),
                aedDevice.getOnUId()
        );
    }
}
