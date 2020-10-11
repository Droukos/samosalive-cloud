package com.droukos.userservice.environment.dto.client.user;

import lombok.*;

@Data @ToString
@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
public class UpdateUserPersonal {
    private String name;
    private String sur;
    private String desc;
    private String ciso;
    private String state;
    private String city;
}
