package com.droukos.aedservice.model.aed_problems;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
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
    private String problemTitle;

    private String address;

    private String information;

    private String status;

    private LocalDateTime uploadedTime;

    private LocalDateTime completedTime;

}