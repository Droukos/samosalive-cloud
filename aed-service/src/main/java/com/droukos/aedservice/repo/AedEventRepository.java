package com.droukos.aedservice.repo;

import com.droukos.aedservice.model.aed_event.AedEvent;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//flatmap
@Repository
public interface AedEventRepository extends ReactiveMongoRepository<AedEvent, String> {
    Flux<AedEvent> findAedEventsByStatus(Integer status);
    Flux<AedEvent> findAedEventsByOccurrenceType(Integer occurrenceType);
    Flux<AedEvent> findAedEventsByOccurrenceTypeAndStatus(Integer occurrenceType, Integer status);
    Flux<AedEvent> findAllByRescuerLike(String rescuer);
}
