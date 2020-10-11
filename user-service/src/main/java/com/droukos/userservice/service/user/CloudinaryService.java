package com.droukos.userservice.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class CloudinaryService {

    /*@Autowired
    private Cloudinary cloudinaryConfig;

    @Value("${cloudinary.folder.avatars}")
    private String cloudFolderAvatars;

    public String uploadFile(String userid, File uploadedFile) {
        try {
            Map uploadResult = cloudinaryConfig.uploader().upload(uploadedFile, ObjectUtils.asMap(
                    "public_id", cloudFolderAvatars + userid,
                    "overwrite", true
            ));
            uploadedFile.delete();

            return uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/
}
