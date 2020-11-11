package com.droukos.userservice.environment.dto.server.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestedUsernameOnly {
  private String user;
}
