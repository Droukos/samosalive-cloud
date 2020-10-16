package com.droukos.userservice.model.user.privacy;

import com.droukos.userservice.environment.constants.FieldNames;
import com.droukos.userservice.environment.dto.client.user.UpdateUserPrivacy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ToString
@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PrivacySettingMap {
  private Map<String, PrivacySetting> privySets;

  public static PrivacySettingMap buildUpdatePrivacy(UpdateUserPrivacy updateUserPrivacy) {
    var map = new HashMap<String, PrivacySetting>();
    map.put(
        FieldNames.F_PRIVY_ONLINE_STATUS, filterPrivacySetting(updateUserPrivacy.getOnStatus()));
    map.put(FieldNames.F_PRIVY_LAST_LOGIN, filterPrivacySetting(updateUserPrivacy.getLstLogin()));
    map.put(FieldNames.F_PRIVY_LAST_LOGOUT, filterPrivacySetting(updateUserPrivacy.getLstLogout()));
    map.put(FieldNames.F_PRIVY_FULLNAME, filterPrivacySetting(updateUserPrivacy.getName()));
    map.put(FieldNames.F_PRIVY_EMAIL, filterPrivacySetting(updateUserPrivacy.getEmail()));
    map.put(FieldNames.F_PRIVY_ACCOUNT_CREATED, filterPrivacySetting(updateUserPrivacy.getAccC()));
    map.put(FieldNames.F_PRIVY_DESCRIPTION, filterPrivacySetting(updateUserPrivacy.getDesc()));
    map.put(FieldNames.F_PRIVY_ADDRESS, filterPrivacySetting(updateUserPrivacy.getAddr()));
    map.put(FieldNames.F_PRIVY_PHONE, filterPrivacySetting(updateUserPrivacy.getPhone()));

    return new PrivacySettingMap(map);
  }

  private static PrivacySetting filterPrivacySetting(PrivacySetting privacySetting) {
    if (privacySetting == null || privacySetting.getUsers() == null) {
      return privacySetting;
    }

    return new PrivacySetting(
        privacySetting.getType(),
        privacySetting.getUsers().stream().map(String::toLowerCase).collect(Collectors.toList()));
  }
}
