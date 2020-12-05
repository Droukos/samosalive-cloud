package com.droukos.aedservice.config;

import com.droukos.aedservice.model.aed_event.AedEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@AllArgsConstructor
public class ReactRedisConfig {

    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        return new LettuceConnectionFactory("127.0.0.1", 6379);
    }

    @Bean
    public ReactiveRedisTemplate<String, AedEvent> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<AedEvent> valueSerializer =
                new Jackson2JsonRedisSerializer<>(AedEvent.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, AedEvent> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, AedEvent> context =
                builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, context);
    }

    @Bean
    public ReactiveRedisTemplate<String, RecordId> reactiveRedisTemplateAedEventIdRecordId(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<RecordId> valueSerializer =
                new Jackson2JsonRedisSerializer<>(RecordId.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, RecordId> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, RecordId> context =
                builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, context);
    }

}
