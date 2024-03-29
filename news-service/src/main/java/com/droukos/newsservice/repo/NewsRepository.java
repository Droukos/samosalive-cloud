package com.droukos.newsservice.repo;

import com.droukos.newsservice.model.news.News;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface NewsRepository extends ReactiveMongoRepository<News, String> {
        Flux<News> findAllByNewsTitleIsContainingAndTagIsNotInOrderByUploadedTimeDesc(String newsTitle, List<Integer> tag);
        Flux<News> findAllByTagIsNotInOrderByUploadedTimeDesc(List<Integer> tag);
        Flux<News> findAllByTagInOrderByUploadedTimeDesc(List<Integer> tag);//briskei oti anikei se toulaxiston mia katigoria
        Flux<News> findAllByTagIs(List<Integer> tag);//briskei akribos autes tis katigories
        Flux<News> findAllByOrderByUploadedTimeDesc();
}

