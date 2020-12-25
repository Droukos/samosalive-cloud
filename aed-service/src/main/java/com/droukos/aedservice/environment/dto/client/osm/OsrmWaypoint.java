package com.droukos.aedservice.environment.dto.client.osm;

import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceAreaLookWithRoute;
import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceInfoPreviewDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import reactor.util.function.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OsrmWaypoint {
    private String aedEventId;
    private String aedDeviceId;
    private List<Waypoints> waypoints;

    public static OsrmWaypoint build(Tuple2<AedDeviceInfoPreviewDto, AedDeviceAreaLookWithRoute> tuple2) {
        AedDeviceInfoPreviewDto dto1 = tuple2.getT1();
        AedDeviceAreaLookWithRoute dto2 = tuple2.getT2();

        return new OsrmWaypoint(
                dto2.getEventId(),
                dto1.getId(),
                Arrays.asList(
                        Waypoints.build(dto2.getRescuerLat(), dto2.getRescuerLng()),
                        Waypoints.build(dto1.getHomePoint().getY(), dto1.getHomePoint().getX()),
                        Waypoints.build(dto2.getEventLat(), dto2.getEventLng())
                )
        );
    }

    public static OsrmWaypoint build(AedDeviceInfoPreviewDto aedDeviceInfoPreviewDto, AedDeviceAreaLookWithRoute dto) {

        return new OsrmWaypoint(
                dto.getEventId(),
                aedDeviceInfoPreviewDto.getId(),
                Arrays.asList(
                        Waypoints.build(dto.getRescuerLat(), dto.getRescuerLng()),
                        Waypoints.build(aedDeviceInfoPreviewDto.getHomePoint().getY(), aedDeviceInfoPreviewDto.getHomePoint().getX()),
                        Waypoints.build(dto.getEventLat(), dto.getEventLng())
                )
        );
    }
}
