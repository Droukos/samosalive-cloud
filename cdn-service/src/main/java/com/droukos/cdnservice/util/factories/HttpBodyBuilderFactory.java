package com.droukos.cdnservice.util.factories;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;

public class HttpBodyBuilderFactory {
    private HttpBodyBuilderFactory() {}

    public static ServerResponse.BodyBuilder okJson(){
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON);
    }
}
