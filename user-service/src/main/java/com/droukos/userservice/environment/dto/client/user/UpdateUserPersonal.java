package com.droukos.userservice.environment.dto.client.user;

import lombok.*;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class UpdateUserPersonal {
    private String userid;
    private String name;
    private String sur;
    private String desc;
    private String ciso;
    private String state;
    private String city;
}
