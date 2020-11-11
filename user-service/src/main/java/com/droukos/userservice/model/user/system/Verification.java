package com.droukos.userservice.model.user.system;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Verification {

  private boolean ver;

  private LocalDateTime verOn;
}
