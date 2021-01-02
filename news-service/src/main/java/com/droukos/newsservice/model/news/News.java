package com.droukos.newsservice.model.news;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@AllArgsConstructor @NoArgsConstructor
@Getter @Document
public class News {

    @Id
    private String id;

    @Indexed
    private String username;

    private String username_canon;

    //private List<Integer> types;

    @Indexed
    private String newsTitle;

    private String content;

    private LocalDateTime uploadedTime;
}
