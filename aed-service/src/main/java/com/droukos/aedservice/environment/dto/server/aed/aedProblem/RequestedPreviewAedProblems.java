package com.droukos.aedservice.environment.dto.server.aed.aedProblem;

//import com.droukos.samosalive.model.problems.Problems;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import lombok.*;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class RequestedPreviewAedProblems {
    private String id;
    private String user;
    private String title;
    private String addr;
    private String info;
    private String status;


    public static RequestedPreviewAedProblems build(AedProblems aedProblems) {
        return new RequestedPreviewAedProblems(
                aedProblems.getId(),
                aedProblems.getUsername(),
                aedProblems.getProblemTitle(),
                aedProblems.getAddress(),
                aedProblems.getInformation(),
                aedProblems.getStatus());
    }
}
