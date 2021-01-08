package com.droukos.newsservice.environment.dto.server.news;

import com.droukos.newsservice.model.news.News;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class RequestedPreviewNews {

    private String id;
    private String user;
    private String title;
    private String content;
    private List<Integer> tag;
    private LocalDateTime uploadedTime;

    public static RequestedPreviewNews build(News news) {
        return new RequestedPreviewNews(
                news.getId(),
                news.getUsername(),
                news.getNewsTitle(),
                news.getContent(),
                news.getTag(),
                news.getUploadedTime()
    );
    }
}
