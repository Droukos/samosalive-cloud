package com.droukos.cdnservice.model.user.privacy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@ToString
@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PrivacySettingMap {
  private Map<String, PrivacySetting> privySets;
}
