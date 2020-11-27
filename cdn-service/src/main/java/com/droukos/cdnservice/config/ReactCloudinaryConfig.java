package com.droukos.cdnservice.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class ReactCloudinaryConfig {

    private final CloudinaryConfigProperties cloudinaryConfigProperties;

    public @Bean
    Cloudinary cloudinaryConfig() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudinaryConfigProperties.getCloudName(),
                "api_key", cloudinaryConfigProperties.getApiKey(),
                "api_secret", cloudinaryConfigProperties.getApiSecret()
        ));
    }

}
