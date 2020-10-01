package com.droukos.authservice.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document
@AllArgsConstructor
@NoArgsConstructor
public class AppState {
  private boolean on;
  private Integer status;
}
