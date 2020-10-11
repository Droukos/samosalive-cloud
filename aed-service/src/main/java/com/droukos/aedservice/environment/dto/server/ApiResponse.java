package com.droukos.aedservice.environment.dto.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApiResponse {

    private int status;
    private String message;
    private Object result;
}
