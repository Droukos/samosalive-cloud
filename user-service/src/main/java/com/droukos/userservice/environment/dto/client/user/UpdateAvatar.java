package com.droukos.userservice.environment.dto.client.user;

import com.droukos.userservice.model.user.UserRes;
import lombok.*;

import java.io.File;

@Data @ToString
@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
public class UpdateAvatar {
    private UserRes user;
    private File av;
}
