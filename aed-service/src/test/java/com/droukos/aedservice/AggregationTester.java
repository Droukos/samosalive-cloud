package com.droukos.aedservice;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoRescuerSub;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.repo.AedEventRepository;
import com.droukos.aedservice.service.aed_event.AedEventInfo;
//import com.google.gson.Gson;
//import com.google.gson.JsonParser;
import com.mongodb.DBObject;
import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@SpringBootTest
public class AggregationTester {
    private static AedEventRepository aedEventRepo;
    private static ReactiveMongoTemplate reactiveMongo;
    private static AedEventInfo aedEventI;

    @BeforeAll
    public static void setupOnce(@Autowired AedEventRepository aedEventRepository,
    @Autowired ReactiveMongoTemplate reactiveMongoTemplate, @Autowired AedEventInfo aedEventInfo) {
        aedEventRepo = aedEventRepository;
        reactiveMongo = reactiveMongoTemplate;
        aedEventI = aedEventInfo;
    }

    @Test
    void aggregationId() {
        //System.out.println(aedEventI.findEventDeviceRescuerAggregation(new AedEventDtoRescuerSub("5fe22ea0740cb86398cc81d1", "kostas","5fdfb2a207f9d125ae099c4a",0 )).blockLast());
        //System.out.println(aedEventRepo.getUnionEventDeviceRescuer("5fe22ea0740cb86398cc81d1", "5fdfb2a207f9d125ae099c4a", "kostas").blockLast());
        //System.out.println(aedEventRepo.getUnionEventDeviceRescuer("5fe22ea0740cb86398cc81d1").blockLast());
        System.out.println(Mono.from(aedEventRepo.getUnionEventDeviceRescuer("5fe22ea0740cb86398cc81d1", "5fdfb2a207f9d125ae099c4a", "kostas")
                ).block());
    }

    @Test
    void aggregationEventPreviewUsers() {
        System.out.println(aedEventRepo.getEventPreviewUsers("5fe4b657ea0a18759850afdb").block());
    }
}
