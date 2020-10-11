package com.droukos.userservice.model.user.system;

import com.droukos.userservice.model.user.system.security.Security;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserSystem {

  private Double credStars;

  private LocalDateTime accC;

  private LocalDateTime accU;

  private Security sec;
}
