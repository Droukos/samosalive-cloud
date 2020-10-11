package com.droukos.userservice.model.user.privacy;

import lombok.*;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@Setter
@Getter
public class PrivacySetting {
  private Integer type;
  private List<String> users;
}
