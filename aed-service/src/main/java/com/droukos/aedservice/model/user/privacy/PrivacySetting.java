package com.droukos.aedservice.model.user.privacy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PrivacySetting {
  private Integer type;
  private List<String> users;
}
