package com.droukos.aedservice.environment.dto.server.aed.aedProblem;

import com.droukos.aedservice.model.aed_problems.AedProblems;
import lombok.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class RequestedPreviewAedProblems {
    private String id;
    private String username;
    private String problemsTitle;
    private String address;
    private String information;
    private Number status;
    private LocalDateTime uploadedTime;

    public static Mono<RequestedPreviewAedProblems> buildMono(AedProblems aedProblems){
        return Mono.just(build(aedProblems));
    }

    public static RequestedPreviewAedProblems build(AedProblems aedProblems) {
        return new RequestedPreviewAedProblems(
                aedProblems.getId(),
                aedProblems.getUsername(),
                aedProblems.getProblemsTitle(),
                aedProblems.getAddress(),
                aedProblems.getInformation(),
                aedProblems.getStatus(),
                aedProblems.getUploadedTime());
    }
}
