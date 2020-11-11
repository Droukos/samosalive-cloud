package com.droukos.cdnservice.model.user.privacy;

import lombok.*;

import java.util.List;

@ToString
@AllArgsConstructor
@Getter
public class PrivacySetting {
  private final Integer type;
  private final List<String> users;

}
