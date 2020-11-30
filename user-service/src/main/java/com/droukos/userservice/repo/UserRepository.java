package com.droukos.userservice.repo;

import com.droukos.userservice.environment.dto.server.user.RequestedUsernameOnly;
import com.droukos.userservice.model.user.UserRes;
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
    Mono<UserRes> findFirstByEmail(String email);
    Mono<UserRes> findFirstByUserLike(String username);
    Flux<RequestedUsernameOnly> findByUserLike(String username);

    @Query("{$and: [{'user': {$regex: ?0, $options: 'i'}}, {'allRoles.role': ?1}]}}")
    Flux<UserRes> findUsersWithUsernameLikeAndRole(String username, String role);
    @Query("{$and: [{'user': {$regex: ?0, $options: 'i'}}, {'allRoles.role': ?1}, {'allRoles.role': ?2}]}}")
    Flux<UserRes> findUsersWithUsernameLikeAnd2Roles(String username, String role1, String role2);
    @Query("{$and: [{'user': {$regex: ?0, $options: 'i'}}, {'allRoles.role': ?1}]}, {'allRoles.role': ?2}, {'allRoles.role': ?3}}")
    Flux<UserRes> findUsersWithUsernameLikeAnd3Roles(String username, String role1, String role2, String role3);
    @Query("{$and: [{'user': {$regex: ?0, $options: 'i'}}, {'allRoles.role': ?1}]}, {'allRoles.role': ?2}, {'allRoles.role': ?3}, {'allRoles.role': ?4}}")
    Flux<UserRes> findUsersWithUsernameLikeAnd4Roles(String username, String role1, String role2, String role3, String rol4);
    @Query("{$and: [{'user': {$regex: ?0, $options: 'i'}}, {'allRoles.role': ?1}]}, {'allRoles.role': ?2}, {'allRoles.role': ?3}, {'allRoles.role': ?4}, {'allRoles.role': ?5}}")
    Flux<UserRes> findUsersWithUsernameLikeAnd5Roles(String username, String role1, String role2, String role3, String role4, String role5);

    @Query("{'allRoles.role': ?0}")
    Flux<UserRes> findUsersWithRole(String role);
    @Query("{$and: [{'allRoles.role': ?0}, {'allRoles.role': ?1}]}")
    Flux<UserRes> findUsersWith2Roles(String role1, String role2);
    @Query("{$and: [{'allRoles.role': ?0}, {'allRoles.role': ?1}, {'allRoles.role': ?2}]}")
    Flux<UserRes> findUsersWith3Roles(String role1, String role2, String role3);
    @Query("{$and: [{'allRoles.role': ?0}, {'allRoles.role': ?1}, {'allRoles.role': ?2}, {'allRoles.role': ?3}]}")
    Flux<UserRes> findUsersWith4Roles(String role1, String role2, String role3, String role4);
    @Query("{$and: [{'allRoles.role': ?0}, {'allRoles.role': ?1}, {'allRoles.role': ?2}, {'allRoles.role': ?3}, {'allRoles.role': ?4}]}")
    Flux<UserRes> findUsersWith5Roles(String role1, String role2, String role3, String role4, String role5);


    Flux<UserRes> findUserByUserLike(String username);
    Flux<UserRes> findFirstByEmailLike(String email);
}
