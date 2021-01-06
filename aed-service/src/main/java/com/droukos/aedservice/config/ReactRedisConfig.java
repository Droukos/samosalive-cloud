package com.droukos.aedservice.config;

import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceInfoPreviewDto;
import com.droukos.aedservice.environment.dto.server.user.RequestedPreviewRescuer;
import com.droukos.aedservice.environment.dto.server.user.RequestedPreviewUser;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.model.aed_event.AedEvent;
import com.droukos.aedservice.model.aed_event.AedEventComment;
import com.droukos.aedservice.model.aed_problems.AedProblems;
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
    public ReactiveRedisTemplate<String, AedEventComment> reactiveRedisTemplateAedComments(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<AedEventComment> valueSerializer =
                new Jackson2JsonRedisSerializer<>(AedEventComment.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, AedEventComment> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, AedEventComment> context =
                builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, context);
    }

    @Bean
    public ReactiveRedisTemplate<String, AedDeviceInfoPreviewDto> reactiveRedisTemplateAedDevices(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<AedDeviceInfoPreviewDto> valueSerializer =
                new Jackson2JsonRedisSerializer<>(AedDeviceInfoPreviewDto.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, AedDeviceInfoPreviewDto> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, AedDeviceInfoPreviewDto> context =
                builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, context);
    }

    @Bean
    public ReactiveRedisTemplate<String, AedProblems> reactiveRedisTemplateAedProblems(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<AedProblems> valueSerializer =
                new Jackson2JsonRedisSerializer<>(AedProblems.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, AedProblems> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, AedProblems> context =
                builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, context);
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
