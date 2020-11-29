package com.droukos.aedservice.repo;

import com.droukos.aedservice.model.aed_device.AedDevice;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AedDeviceRepository  extends ReactiveMongoRepository<AedDevice, String> {
    Mono<AedDevice> getAedDeviceById(String id);
    Mono<AedDevice> getAedDeviceByUniqNickname(String uniqNickname);
    Flux<AedDevice> getAedDevicesByUniqNicknameLike(String uniqNickname);
    Flux<AedDevice> getAedDevicesByHomePNear(Point p, Distance d);
}
