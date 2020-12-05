package com.droukos.aedservice.model.factories.aed_problems;

import com.droukos.aedservice.environment.dto.client.aed_problems.AedProblemsDtoTechnicalSub;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;

import static com.droukos.aedservice.environment.enums.AedEventStatus.ONPROGRESS;

public class AedProblemsFactorySubTechnical {

    private AedProblemsFactorySubTechnical() {}

    public static Mono<AedProblems> subTechnicalMono(Tuple2<AedProblems, AedProblemsDtoTechnicalSub> tuple2){
        return Mono.just(subTechnical(tuple2));
    }
    public static AedProblems subTechnical(Tuple2<AedProblems, AedProblemsDtoTechnicalSub> tuple2){
        AedProblems aedProblems = tuple2.getT1();
        String technical = tuple2.getT2().getTechnical();
        return new AedProblems(
                aedProblems.getId(),
                aedProblems.getUsername(),
                aedProblems.getUsername_canon(),
                aedProblems.getTitle(),
                aedProblems.getBody(),
                aedProblems.getMapPoint(),
                aedProblems.getAddress(),
                ONPROGRESS.getStatus(),
                technical,
                aedProblems.getUploadedTime(),
                LocalDateTime.now(),
                aedProblems.getCompletedTime(),
                aedProblems.getConclusion()
        );
    }
}
