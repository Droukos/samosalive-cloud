package com.droukos.aedservice.model.factories.aed_problems;

import com.droukos.aedservice.environment.dto.client.aed_problems.AedProblemsDtoClose;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;

import static com.droukos.aedservice.environment.enums.AedEventStatus.COMPLETED;

public class AedProblemsFactoryClose {
    private AedProblemsFactoryClose() {}

    public static Mono<AedProblems> closeAedProblems(Tuple2<AedProblems, AedProblemsDtoClose> tuple2){
        return Mono.just(closeProblems(tuple2));
    }
    public static AedProblems closeProblems(Tuple2<AedProblems, AedProblemsDtoClose> tuple2){
        AedProblems aedProblems = tuple2.getT1();
        String conclusion = tuple2.getT2().getConclusion();
        return new AedProblems(
                aedProblems.getId(),
                aedProblems.getUsername(),
                aedProblems.getUsername_canon(),
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
}
