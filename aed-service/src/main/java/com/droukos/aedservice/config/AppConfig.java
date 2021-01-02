package com.droukos.aedservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfig {
    @Value("${samosalive.comments.offset}")
    private long commentsOffset;
}
