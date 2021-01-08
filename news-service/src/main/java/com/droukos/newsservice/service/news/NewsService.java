package com.droukos.newsservice.service.news;

import com.droukos.newsservice.model.news.News;
import com.droukos.newsservice.repo.NewsRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.droukos.newsservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@RequiredArgsConstructor
@Getter
public class NewsService {

    @NonNull private final NewsRepository newsRepository;

    public void initializeNews(List<News> news) {
        newsRepository.saveAll(news).subscribe();
    }

    public Flux<News> getAllNews() {
        return newsRepository.findAll();
    }

    public Mono<News> save(News news) {
        return newsRepository.save(news);
    }

    //public Flux<News> getNewsByNewsTitle(String newsTitle) {
    //    return newsRepository.findAllByNewsTitleIsContainingAndTagIsNot(newsTitle,0);
    //}
    //public Mono<News> findNewsById(String id) {
    //    return newsRepository.findById(id)
    //            .switchIfEmpty(Mono.error(badRequest("News does not exists")));
    //}

    //public Mono<News> validateNews (ServerRequest request){
    //    return request.bodyToMono(News.class)
    //            .defaultIfEmpty(new News())
    //            .flatMap(news -> {
    //                ValidatorUtil.validate(news, new NewsCreationValidator());
    //                return Mono.just(news);
    //            });
    //}
//
    //public Mono<News> validateTitle (ServerRequest request){
    //    return request.bodyToMono(News.class)
    //            .defaultIfEmpty(new News())
    //            .flatMap(news -> {
    //                ValidatorUtil.validate(news, new NewsTitleValidator());
    //                return Mono.just(news);
    //            });
    //}

    public Mono<News> createNewsDto(ServerRequest request){
        return request.bodyToMono(News.class).defaultIfEmpty(new News());
    }
}
