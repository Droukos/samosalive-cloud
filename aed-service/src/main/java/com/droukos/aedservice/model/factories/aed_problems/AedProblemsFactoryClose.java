package com.droukos.aedservice.model.factories.aed_problems;

import com.droukos.aedservice.environment.dto.client.aed_problems.AedProblemsDtoClose;
import com.droukos.aedservice.environment.enums.DeviceAvailability;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import java.time.LocalDateTime;

import static com.droukos.aedservice.environment.enums.AedEventStatus.COMPLETED;

public class AedProblemsFactoryClose {
    private AedProblemsFactoryClose() {}

    public static Mono<Tuple2<AedProblems, AedDevice>> closeAedDeviceProblem(Tuple3<AedProblems, AedProblemsDtoClose, AedDevice> tuple3){
        return Mono.zip(Mono.just(closeProblems(tuple3)), Mono.just(closeAedDevice(tuple3)));
    }
    public static AedProblems closeProblems(Tuple3<AedProblems, AedProblemsDtoClose, AedDevice> tuple3){
        AedProblems aedProblems = tuple3.getT1();
        String conclusion = tuple3.getT2().getConclusion();
        return new AedProblems(
                aedProblems.getId(),
                aedProblems.getUsername(),
                aedProblems.getUsername_canon(),
                aedProblems.getAedDevId(),
                aedProblems.getTitle(),
                aedProblems.getBody(),
                aedProblems.getMapPoint(),
                aedProblems.getAddress(),
                COMPLETED.getStatus(),
                aedProblems.getTechnical(),
                aedProblems.getUploadedTime(),
                aedProblems.getAcceptedTime(),
                LocalDateTime.now(),
                conclusion
        );
    }

    public static AedDevice closeAedDevice(Tuple3<AedProblems, AedProblemsDtoClose, AedDevice> tuple3) {
        AedDevice aedDevice = tuple3.getT3();
        return new AedDevice(
                aedDevice.getId(),
                aedDevice.getUniqNickname(),
                aedDevice.getModelName(),
                aedDevice.getDesc(),
                aedDevice.getAdded(),
                aedDevice.getAddedBy(),
                DeviceAvailability.AVAILABLE.getCode(),
                null,
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
