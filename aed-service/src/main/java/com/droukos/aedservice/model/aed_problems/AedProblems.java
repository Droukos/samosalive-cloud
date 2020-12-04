package com.droukos.aedservice.model.aed_problems;

import com.droukos.aedservice.util.GeoJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor @NoArgsConstructor
@Getter @Document
public class AedProblems {

    @Id
    private String id;

    @Indexed
    private String username;

    private String username_canon;

    @Indexed
    private String title;

    private String body;

    @JsonDeserialize(using = GeoJsonDeserializer.class)
    private GeoJsonPoint mapPoint;

    private String address;

    private Integer status;

    private String technical;

    private LocalDateTime uploadedTime;

    private LocalDateTime acceptedTime;

    private LocalDateTime completedTime;

    private String conclusion;

}
