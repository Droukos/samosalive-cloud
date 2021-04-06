package com.droukos.authservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReactMongoConfig{

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

    //public @Bean ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory() {
    //    return new SimpleReactiveMongoDatabaseFactory(MongoClients.create("mongodb://samosalive-mongodb"), database);
    //}
//
    //public @Bean ReactiveMongoTemplate reactiveMongoTemplate() {
    //    return new ReactiveMongoTemplate(reactiveMongoDatabaseFactory());
    //}

}
