package com.droukos.authservice.environment.dto.server;

import lombok.*;

@Data @ToString
@AllArgsConstructor @NoArgsConstructor
@Setter @Getter
public class ApiResponse {

    private int status;
    private String message;
    private Object result;

}
