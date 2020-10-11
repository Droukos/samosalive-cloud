package com.droukos.newsservice.environment.dto.server.news;

import com.droukos.newsservice.model.news.News;
import lombok.*;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class RequestedPreviewNews {

    private String id;
    private String user;
    private String title;
    private String cont;

    public static RequestedPreviewNews build(News news) {
        return new RequestedPreviewNews(
                news.getId(),
                news.getUsername(),
                news.getNewsTitle(),
                news.getContent());
    }
}
