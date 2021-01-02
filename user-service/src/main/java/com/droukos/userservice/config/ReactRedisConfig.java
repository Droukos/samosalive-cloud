package com.droukos.userservice.config;

import com.droukos.userservice.environment.dto.server.user.RequestedPreviewRescuer;
import com.droukos.userservice.environment.dto.server.user.RequestedPreviewUser;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@AllArgsConstructor
public class ReactRedisConfig {
    //@Value("${samosalive.redis.host}")
    //private final String host;
    //@Value("${samosalive.redis.port}")
    //private final int port;

    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }

    @Bean
    public ReactiveRedisTemplate<String, RequestedPreviewRescuer> reactiveRedisTemplateRescuer(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<RequestedPreviewRescuer> valueSerializer =
                new Jackson2JsonRedisSerializer<>(RequestedPreviewRescuer.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, RequestedPreviewRescuer> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, RequestedPreviewRescuer> context =
                builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, context);
    }

    @Bean
    public ReactiveRedisTemplate<String, RequestedPreviewUser> reactiveRedisTemplateUser(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<RequestedPreviewUser> valueSerializer =
                new Jackson2JsonRedisSerializer<>(RequestedPreviewUser.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, RequestedPreviewUser> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, RequestedPreviewUser> context =
                builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, context);
    }
}
