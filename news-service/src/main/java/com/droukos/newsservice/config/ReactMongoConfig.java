package com.droukos.newsservice.config;

import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;

@Configuration
public class ReactMongoConfig {

    @Value("${mongodb.host}")
    private String host;
    @Value("${mongodb.port}")
    private int port;
    @Value("${mongodb.database}")
    private String database;
    @Value("${mongodb.username}")
    private String username;
    @Value("${mongodb.password}")
    private String password;

    //public @Bean
    //ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory() {
    //    return new SimpleReactiveMongoDatabaseFactory(MongoClients.create("mongodb://localhost"), database);
    //}
//
    //public @Bean
    //ReactiveMongoTemplate reactiveMongoTemplate() {
    //    return new ReactiveMongoTemplate(reactiveMongoDatabaseFactory());
    //}

}
