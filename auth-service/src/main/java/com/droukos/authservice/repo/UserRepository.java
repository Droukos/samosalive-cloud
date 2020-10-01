package com.droukos.authservice.repo;

import com.droukos.authservice.model.user.UserRes;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserRes, String> {

    Mono<UserRes> findFirstById(String id);
    Mono<UserRes> findFirstByUser(String username);
    Mono<UserRes> findFirstByEmail(String email);
}
