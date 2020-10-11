package com.droukos.userservice.environment.dto.client.user;

import com.droukos.userservice.model.user.privacy.PrivacySetting;
import lombok.*;

@Data @ToString
@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
public class UpdateUserPrivacy {

    private PrivacySetting onStatus;
    private PrivacySetting lstLogin;
    private PrivacySetting lstLogout;
    private PrivacySetting name;
    private PrivacySetting addr;
    private PrivacySetting email;
    private PrivacySetting accC;
    private PrivacySetting desc;
    private PrivacySetting phone;

}
