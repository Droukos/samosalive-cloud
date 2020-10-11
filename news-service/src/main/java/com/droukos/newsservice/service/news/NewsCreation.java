package com.droukos.newsservice.service.news;

import com.droukos.newsservice.environment.dto.client.NewsDtoCreate;
import com.droukos.newsservice.model.news.News;
import com.droukos.newsservice.environment.constants.StatusCodes;
import com.droukos.newsservice.environment.dto.server.ApiResponse;
import com.droukos.newsservice.repo.NewsRepository;
import com.droukos.newsservice.service.validator.NewsCreationValidator;
import com.droukos.newsservice.util.ValidatorUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static com.droukos.newsservice.model.factories.NewsFactoryCreate.newsCreate;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@RequiredArgsConstructor
public class NewsCreation {
    @NonNull private final NewsRepository newsRepository;

    public Mono<News> createNews (NewsDtoCreate newsDtoCreate){
        return Mono.just(newsCreate (newsDtoCreate) );
    }
    public void validateNews (NewsDtoCreate newsDtoCreate) {
        ValidatorUtil.validate(newsDtoCreate, new NewsCreationValidator());
    }

    public Mono<ServerResponse> saveNews(News news){
        Function<News, Mono<ServerResponse>> result = savedNews -> ok().contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(new ApiResponse(StatusCodes.OK, "News created", "news.created")));

        return newsRepository.save(news).flatMap(result);
    }
}
