package com.droukos.newsservice.service.news;

import com.droukos.newsservice.environment.dto.client.NewsDtoSearch;
import com.droukos.newsservice.environment.dto.server.news.RequestedPreviewNews;
import com.droukos.newsservice.model.news.News;
import com.droukos.newsservice.repo.NewsRepository;
import com.droukos.newsservice.service.validator.NewsTitleValidator;
import com.droukos.newsservice.util.ValidatorUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NewsInfo {
    @NonNull private final NewsRepository newsRepository;

    public void validateTitle (NewsDtoSearch newsDtoSearch){
        ValidatorUtil.validate(newsDtoSearch, new NewsTitleValidator());
    }

    public Flux<News> findNewsByTitle(NewsDtoSearch newsDtoSearch) {
        return newsRepository.findAllByNewsTitleIsContaining(newsDtoSearch.getNewsTitle());
    }

    public Mono<RequestedPreviewNews> fetchNewsByTitle(News news){
        return Mono.just(RequestedPreviewNews.build(news));
    }
}
