package com.droukos.aedservice.repo;

import com.droukos.aedservice.model.user.UserRes;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserRes, String> {
    Mono<UserRes> findFirstByUser(String username);
    Flux<UserRes> findAllByUserIn(List<String> usernames);
}
