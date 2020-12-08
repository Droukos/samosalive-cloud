package com.droukos.cdnservice.repo;

import com.droukos.cdnservice.model.user.UserRes;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserRes, String> {

    Mono<UserRes> findFirstById(String id);

    @Query("{ $or:  [{ 'id' : ?0 }, { 'id': ?1 }]}")
    Flux<UserRes> findUserAndTargetUser(String id, String targetId);

    //Flux<User> findUserAndTargetUsers(String id, )
    Mono<UserRes> findFirstByUser(String username);
}
