package com.droukos.aedservice.environment.dto.server.aed.aedProblem;

import com.droukos.aedservice.model.aed_problems.AedProblems;
import com.droukos.aedservice.util.GeoJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.data.geo.Point;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class RequestedPreviewAedProblems {
    private String id;
    private String username;
    private String title;
    private String body;
    @JsonDeserialize(using = GeoJsonDeserializer.class)
    private Point mapPoint;
    private String address;
    private Number status;
    private LocalDateTime uploadedTime;

    public static Mono<RequestedPreviewAedProblems> buildMono(AedProblems aedProblems){
        return Mono.just(build(aedProblems));
    }

    public static RequestedPreviewAedProblems build(AedProblems aedProblems) {
        return new RequestedPreviewAedProblems(
                aedProblems.getId(),
                aedProblems.getUsername(),
                aedProblems.getTitle(),
                aedProblems.getBody(),
                aedProblems.getMapPoint(),
                aedProblems.getAddress(),
                aedProblems.getStatus(),
                aedProblems.getUploadedTime());
    }
}
