package com.droukos.newsservice.service.news;

import com.droukos.newsservice.environment.dto.client.NewsDtoIdSearch;
import com.droukos.newsservice.environment.dto.client.NewsDtoSearch;
import com.droukos.newsservice.environment.dto.server.news.RequestedNews;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.droukos.newsservice.environment.constants.TagList.IDEAS;
import static com.droukos.newsservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@RequiredArgsConstructor
public class NewsInfo {
    @NonNull
    private final NewsRepository newsRepository;

    public void validateTitle(NewsDtoSearch newsDtoSearch) {
        ValidatorUtil.validate(newsDtoSearch, new NewsTitleValidator());
    }

    public Flux<News> findNewsByTitleOrTag(NewsDtoSearch newsDtoSearch) {

        List<Integer> ideaslist = new ArrayList<>();
        ideaslist.add(0);
        List<Integer> emptylist = new ArrayList<>();
        emptylist.add(-1);
        List<Integer> everything = new ArrayList<>();
        everything.add(1);
        everything.add(2);
        everything.add(3);
        everything.add(4);

        List<Integer>tagList = newsDtoSearch.getSearchTag();
        Collections.sort(tagList);

        if(!newsDtoSearch.getNewsTitle().equals("")){
            return newsRepository.findAllByNewsTitleIsContainingAndTagIsNotIn(newsDtoSearch.getNewsTitle(),ideaslist);
        }
        else if(emptylist.equals(tagList)){
            return newsRepository.findAllByTagIsNotIn(ideaslist);
        }
        else {
            return newsRepository.findAllByTagIn(newsDtoSearch.getSearchTag());
        }
    }

    public Flux<News> findNewsByUploadDesc() {
        return newsRepository.findAllByOrderByUploadedTimeDesc();
    }

    public Mono<RequestedPreviewNews> fetchNews(News news) {
        return Mono.just(RequestedPreviewNews.build(news));
    }

    public Mono<News> findNewsById(String id) {
        return newsRepository.findById(id)
                .defaultIfEmpty(new News())
                .flatMap(news -> news.getId() == null ? Mono.error(badRequest("News not found")) : Mono.just(news));
    }

    public Mono<RequestedNews> fetchNewsById(News news) {
        return Mono.just(RequestedNews.build(news));
    }
}
