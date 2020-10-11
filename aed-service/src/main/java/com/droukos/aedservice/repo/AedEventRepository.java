package com.droukos.aedservice.repo;

import com.droukos.aedservice.model.aed_event.AedEvent;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

//flatmap
@Repository
public interface AedEventRepository extends ReactiveMongoRepository<AedEvent, String> {
    Flux<AedEvent> findAllByOccurrenceTypeIsLike(String occurrenceTpe);
    Flux<AedEvent> findAllByRescuerLike(String rescuer);
}
