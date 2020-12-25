package com.droukos.aedservice.environment.dto.server.aed.aed_device;

import com.droukos.aedservice.environment.dto.server.aed.osrm.RouteInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AedDevicePreviewWithRouteDto {
    private String aedEventId;
    private AedDeviceInfoPreviewDto aedDeviceInfoPreviewDto;
    private String routeInfo;

    public static Mono<AedDevicePreviewWithRouteDto> buildMono(Tuple2<RouteInfoDto, AedDeviceInfoPreviewDto> tuple2) {
        return Mono.just(build(tuple2));
    }

    public static AedDevicePreviewWithRouteDto build(Tuple2<RouteInfoDto, AedDeviceInfoPreviewDto> tuple2) {
        return new AedDevicePreviewWithRouteDto(
                tuple2.getT1().getAedEventId(),
                tuple2.getT2(),
                tuple2.getT1().getRouteInfo()
        );
    }
}
