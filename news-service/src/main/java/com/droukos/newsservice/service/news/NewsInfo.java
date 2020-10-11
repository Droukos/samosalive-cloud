package com.droukos.newsservice.service.news;

import com.droukos.newsservice.environment.dto.server.news.RequestedPreviewNews;
import com.droukos.newsservice.model.news.News;
import com.droukos.newsservice.repo.NewsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@RequiredArgsConstructor
public class NewsInfo {
    @NonNull private final NewsRepository newsRepository;

    //public Mono<News> validateTitle (News news){
    //    validate(news, new NewsTitleValidator());
    //    return Mono.just(news);
    //}

    public Mono<ServerResponse> fetchNewsByTitle (News news){
        Function<String, Flux<RequestedPreviewNews>> fetchNewsByType = newsTitle ->
                newsRepository.findAllByNewsTitleIsContaining(newsTitle)
                        .flatMap(fetchedNews -> Mono.just(RequestedPreviewNews.build(fetchedNews)));

        Function<Flux<RequestedPreviewNews>, Mono<ServerResponse>> result = requested_previewAedEvent ->
                ok().body(requested_previewAedEvent, RequestedPreviewNews.class);

        return result.apply(fetchNewsByType.apply(news.getNewsTitle()));
    }
}
