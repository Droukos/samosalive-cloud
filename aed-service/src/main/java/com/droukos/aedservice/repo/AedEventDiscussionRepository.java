package com.droukos.aedservice.repo;

import com.droukos.aedservice.model.aed_event.AedEventComment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AedEventDiscussionRepository extends ReactiveMongoRepository<AedEventComment, String> {

    Flux<AedEventComment> getAedEventCommentsByEventId(String eventId);

}
