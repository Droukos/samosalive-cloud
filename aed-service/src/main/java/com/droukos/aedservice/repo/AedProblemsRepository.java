package com.droukos.aedservice.repo;

import com.droukos.aedservice.model.aed_problems.AedProblems;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AedProblemsRepository extends ReactiveMongoRepository<AedProblems, String> {
    Flux<AedProblems> findAllByProblemsTitleContaining(String problemTitle);
}
