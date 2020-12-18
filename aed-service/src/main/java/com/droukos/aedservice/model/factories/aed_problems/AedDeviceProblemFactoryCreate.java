package com.droukos.aedservice.model.factories.aed_problems;

import com.droukos.aedservice.environment.dto.client.aed_problems.AedDeviceProblemDtoCreate;
import com.droukos.aedservice.environment.enums.DeviceAvailability;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;

import static com.droukos.aedservice.environment.enums.AedEventStatus.PENDING;

public class AedDeviceProblemFactoryCreate {

    private AedDeviceProblemFactoryCreate(){}

    public static Mono<Tuple2<AedProblems, AedDevice>> aedDeviceBrokenProblemMono(Tuple2<AedDeviceProblemDtoCreate, AedDevice> tuple2) {
        return Mono.zip(Mono.just(problemsCreate(tuple2)), Mono.just(aedDeviceBrokenProblemCreate(tuple2)));
    }

    public static AedProblems problemsCreate(Tuple2<AedDeviceProblemDtoCreate, AedDevice> tuple2){
        AedDeviceProblemDtoCreate dto = tuple2.getT1();
        return new AedProblems(null,
                dto.getUsername().toLowerCase(),
                dto.getUsername(),
                dto.getAedDeviceId(),
                dto.getTitle(),
                dto.getBody(),
                new GeoJsonPoint(dto.getX(), dto.getY()),
                dto.getAddress(),
                PENDING.getStatus(),
                null,
                LocalDateTime.now(),
                null,
                null,
                null
        );
    }

    public static AedDevice aedDeviceBrokenProblemCreate(Tuple2<AedDeviceProblemDtoCreate, AedDevice> tuple2) {
        AedDevice aedDevice = tuple2.getT2();
        return new AedDevice(
                aedDevice.getId(),
                aedDevice.getUniqNickname(),
                aedDevice.getModelName(),
                aedDevice.getDesc(),
                aedDevice.getAdded(),
                aedDevice.getAddedBy(),
                DeviceAvailability.BROKEN.getCode(),
                tuple2.getT1().getBody(),
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
