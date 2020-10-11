package com.droukos.userservice.model.user.privacy;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data @ToString @Document
@NoArgsConstructor @AllArgsConstructor
public class PrivacySettingMap {
    private Map<String, PrivacySetting> privySets;

}
