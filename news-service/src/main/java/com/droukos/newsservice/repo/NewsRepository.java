package com.droukos.newsservice.repo;

import com.droukos.newsservice.model.news.News;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface NewsRepository extends ReactiveMongoRepository<News, String> {
        Flux<News> findAllByNewsTitleIsContaining(String newsTitle);
}

