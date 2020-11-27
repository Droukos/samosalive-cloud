package com.droukos.userservice.environment.dto.client.user;

import com.droukos.userservice.model.user.privacy.PrivacySetting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateUserPrivacy {

  private String userid;
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
