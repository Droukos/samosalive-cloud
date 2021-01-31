package com.droukos.authservice.repo;

import com.droukos.authservice.model.sub.UserSub;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface UserSubsRepository extends ReactiveMongoRepository<UserSub, String> {
     Mono<UserSub> getUserSubById(String id);
     Flux<UserSub> getUserSubsByIdIn(List<String> ids);
}