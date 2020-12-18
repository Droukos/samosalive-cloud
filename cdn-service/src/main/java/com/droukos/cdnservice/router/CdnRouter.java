package com.droukos.cdnservice.router;

import com.droukos.cdnservice.controller.CdnHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.droukos.cdnservice.environment.services.AedDeviceServices.*;
import static com.droukos.cdnservice.environment.services.AvatarServices.PUT_AVATAR_PIC;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CdnRouter {

    @Bean
    public RouterFunction<ServerResponse> newsRoute(CdnHandler cdnHandler) {
        return route()
                .path("api/cdn/", builder -> builder
                        .PUT(PUT_AED_DEVICE_PICS.getUrl(), accept(APPLICATION_JSON), cdnHandler::putNewAedDevicePics)
                        .PUT(PUT_AVATAR_PIC.getUrl(), accept(APPLICATION_JSON), cdnHandler::putNewAvatarPic)
                        .PUT(PUT_AED_DEVICE_PIC.getUrl(), accept(APPLICATION_JSON), cdnHandler::putNewAedDevicePic)
                        .PUT(PUT_AED_DEVICE_ADDRESS_PIC.getUrl(), accept(APPLICATION_JSON), cdnHandler::putNewAedDeviceAddressPic)
                ).build();
    }
}