package com.droukos.authservice.repo;

import com.droukos.authservice.model.user.UserRes;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserRes, String> {

    Mono<UserRes> findFirstById(String id);
    Mono<UserRes> findFirstByUser(String username);
    Mono<UserRes> findFirstByEmail(String email);
    Mono<UserRes> findFirstByUserOrEmail(String username, String email);
    Flux<UserRes> findAllByUserIn(List<String> usernames);
}
