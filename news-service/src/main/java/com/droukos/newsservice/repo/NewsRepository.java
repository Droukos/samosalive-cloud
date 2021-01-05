package com.droukos.newsservice.repo;

import com.droukos.newsservice.model.news.News;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface NewsRepository extends ReactiveMongoRepository<News, String> {
        Flux<News> findAllByNewsTitleIsContainingAndTagIsNot(String newsTitle, Integer tag);
        Flux<News> findAllByTag(Integer tag);
        Flux<News> findAllByOrderByUploadedTimeDesc();
}

