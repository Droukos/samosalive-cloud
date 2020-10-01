package com.droukos.authservice.environment.dto.client.auth;

import lombok.*;

import java.util.List;

@Data @ToString
@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
public class UpdateRole {
    private String updatedRole;
    private List<String> rolesOnDb;
}
