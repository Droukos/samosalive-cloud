package com.droukos.aedservice.model.factories.aed_problems;

import com.droukos.aedservice.environment.dto.client.aed_problems.AedProblemsDtoCreate;
import com.droukos.aedservice.model.aed_problems.AedProblems;

import java.time.LocalDateTime;

public class AedProblemsFactoryCreate {

    private AedProblemsFactoryCreate(){}

    public static AedProblems problemsCreate(AedProblemsDtoCreate aedProblemsDtoCreate){
        return new AedProblems(null,
                aedProblemsDtoCreate.getUsername().toLowerCase(),
                aedProblemsDtoCreate.getUsername(),
                aedProblemsDtoCreate.getProblemsTitle(),
                aedProblemsDtoCreate.getAddress(),
                aedProblemsDtoCreate.getInformation(),
                aedProblemsDtoCreate.getStatus(),
                null,
                LocalDateTime.now(),
                null,
                null,
                null
        );
    }

}
