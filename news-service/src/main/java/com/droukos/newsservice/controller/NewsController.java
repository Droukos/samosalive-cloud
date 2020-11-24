package com.droukos.newsservice.controller;

import com.droukos.newsservice.environment.dto.client.NewsDtoCreate;
import com.droukos.newsservice.environment.dto.client.NewsDtoIdSearch;
import com.droukos.newsservice.environment.dto.client.NewsDtoSearch;
import com.droukos.newsservice.environment.dto.server.news.RequestedNews;
import com.droukos.newsservice.environment.dto.server.news.RequestedPreviewNews;
import com.droukos.newsservice.model.news.News;
import com.droukos.newsservice.service.news.NewsCreation;
import com.droukos.newsservice.service.news.NewsInfo;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class NewsController {

    private final NewsCreation newsCreation;
    private final NewsInfo newsInfo;

    @MessageMapping("news.post")
    public Mono<Boolean> createNews(NewsDtoCreate newsDtoCreate){
        return Mono.just(newsDtoCreate)
                .doOnNext(newsCreation::validateNews)
                .flatMap(newsCreation::createNews)
                .flatMap(newsCreation::saveNews);
    }
    @MessageMapping("news.get")
    public Flux<RequestedPreviewNews> findNews(NewsDtoSearch newsDtoSearch){
        return Flux.just(newsDtoSearch)
                .doOnNext(newsInfo::validateTitle)
                .flatMap(newsInfo::findNewsByTitle)
                .flatMap(newsInfo::fetchNewsByTitle);
    }
    @MessageMapping("news.getId")
    public Mono<RequestedNews> findNewsById(NewsDtoIdSearch newsId){
        return Mono.just(newsId.getId())
                .flatMap(newsInfo::findNewsById)
                .flatMap(RequestedNews::buildMono);
    }
}
