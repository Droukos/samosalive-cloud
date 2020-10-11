package com.droukos.newsservice.controller;

import com.droukos.newsservice.environment.dto.client.NewsDtoCreate;
import com.droukos.newsservice.model.news.News;
import com.droukos.newsservice.service.news.NewsCreation;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class NewsController {

    private final NewsCreation newsCreation;

    @MessageMapping("news.createNews")
    public Mono<News> createNews(NewsDtoCreate newsDtoCreate){
        return Mono.just(newsDtoCreate)
                .doOnNext(newsCreation::validateNews)
                .flatMap(newsCreation::createNews);
    }
}
