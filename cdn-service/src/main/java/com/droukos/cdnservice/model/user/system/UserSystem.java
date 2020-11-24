package com.droukos.cdnservice.model.user.system;

import com.droukos.cdnservice.model.user.system.security.Security;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSystem {

    private Double credStars;

    private LocalDateTime accC;

    private LocalDateTime accU;

    private Security sec;
}
