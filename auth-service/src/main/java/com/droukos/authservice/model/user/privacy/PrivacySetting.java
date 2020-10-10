package com.droukos.authservice.model.user.privacy;

import lombok.*;

import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PrivacySetting {
  private Integer type;
  private List<String> users;
}
