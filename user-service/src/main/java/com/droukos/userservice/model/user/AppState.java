package com.droukos.userservice.model.user;

import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AppState {
  private boolean on;
  private Integer status;
}
