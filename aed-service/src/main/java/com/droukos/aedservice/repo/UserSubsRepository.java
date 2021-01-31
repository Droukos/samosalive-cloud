package com.droukos.aedservice.repo;

import com.droukos.aedservice.model.sub.UserSub;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubsRepository extends ReactiveMongoRepository<UserSub, String> {
}
