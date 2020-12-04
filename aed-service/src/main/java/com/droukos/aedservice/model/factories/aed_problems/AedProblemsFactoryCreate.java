package com.droukos.aedservice.model.factories.aed_problems;

import com.droukos.aedservice.environment.dto.client.aed_problems.AedProblemsDtoCreate;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;

import static com.droukos.aedservice.environment.enums.AedEventStatus.PENDING;

public class AedProblemsFactoryCreate {

    private AedProblemsFactoryCreate(){}

    public static AedProblems problemsCreate(AedProblemsDtoCreate aedProblemsDtoCreate){
        return new AedProblems(null,
                aedProblemsDtoCreate.getUsername().toLowerCase(),
                aedProblemsDtoCreate.getUsername(),
                aedProblemsDtoCreate.getTitle(),
                aedProblemsDtoCreate.getBody(),
                new GeoJsonPoint(aedProblemsDtoCreate.getX(), aedProblemsDtoCreate.getY()),
                aedProblemsDtoCreate.getAddress(),
                PENDING.getStatus(),
                null,
                LocalDateTime.now(),
                null,
                null,
                null
        );
    }

}
