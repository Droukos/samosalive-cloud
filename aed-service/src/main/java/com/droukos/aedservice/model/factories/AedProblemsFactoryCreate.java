package com.droukos.aedservice.model.factories;

import com.droukos.aedservice.environment.dto.client.aed_problems.AedProblemsDtoCreate;
import com.droukos.aedservice.model.aed_problems.AedProblems;

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
                aedProblemsDtoCreate.getUploadedTime(),
                null
        );
    }

}
