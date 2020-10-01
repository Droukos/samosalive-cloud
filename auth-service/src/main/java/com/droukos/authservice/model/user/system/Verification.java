package com.droukos.authservice.model.user.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@ToString
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Verification {

  private boolean ver;

  private LocalDateTime verOn;
}
