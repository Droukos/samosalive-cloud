package com.droukos.aedservice.repo;

import com.droukos.aedservice.model.aed_device.AedDevice;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AedDeviceRepository  extends ReactiveMongoRepository<AedDevice, String> {
    Mono<AedDevice> getAedDeviceById(String id);
}
