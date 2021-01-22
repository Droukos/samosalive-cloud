package com.droukos.aedservice.environment.dto.server.aed.aedProblem;

import com.droukos.aedservice.model.aed_problems.AedProblems;
import com.droukos.aedservice.util.GeoJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.geo.Point;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class RequestedAedProblems {
    private String id;
    private String username;
    private Integer title;
    private String body;
    @JsonDeserialize(using = GeoJsonDeserializer.class)
    private Point point;
    private String address;
    private Integer status;
    private String technical;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime uploadedTime;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime completedTime;
    private String conclusion;

    public static Mono<RequestedAedProblems> buildMono(AedProblems aedProblems){
        return Mono.just(build(aedProblems));
    }

    public static RequestedAedProblems build(AedProblems aedProblems) {
        return new RequestedAedProblems(
                aedProblems.getId(),
                aedProblems.getUsername(),
                aedProblems.getTitle(),
                aedProblems.getBody(),
                aedProblems.getMapPoint(),
                aedProblems.getAddress(),
                aedProblems.getStatus(),
                aedProblems.getTechnical(),
                aedProblems.getUploadedTime(),
                aedProblems.getCompletedTime(),
                aedProblems.getConclusion());

    }
}
