package com.droukos.cdnservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class CloudinaryConfigProperties {

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    @Value("${cloudinary.folder.avatars}")
    private String cloudFolderAvatars;

    @Value("${cloudinary.folder.address}")
    private String cloudFolderAddress;

    @Value("${cloudinary.folder.device}")
    private String cloudFolderDevice;
}
