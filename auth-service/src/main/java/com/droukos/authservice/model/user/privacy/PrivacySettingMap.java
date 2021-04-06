package com.droukos.authservice.model.user.privacy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PrivacySettingMap {
  private Map<String, PrivacySetting> privySets;
}
