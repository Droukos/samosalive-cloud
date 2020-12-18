package com.droukos.newsservice.environment.dto.server.news;

import com.droukos.newsservice.model.news.News;
import lombok.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestedNews {
    private String id;
    private String username;
    private String newsTitle;
    private String content;
    private LocalDateTime uploadedTime;

    public static Mono<RequestedNews> buildMono(News news){
        return Mono.just(build(news));
    }

    public static RequestedNews build(News news) {
        return new RequestedNews(
                news.getId(),
                news.getUsername(),
                news.getNewsTitle(),
                news.getContent(),
                news.getUploadedTime()
        );
    }
}
