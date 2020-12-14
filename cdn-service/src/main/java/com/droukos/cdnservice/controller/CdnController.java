package com.droukos.cdnservice.controller;

import com.droukos.cdnservice.environment.dto.client.UpdateAvatar;
import com.droukos.cdnservice.service.cdn.AvatarService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;

@Controller
@AllArgsConstructor
public class CdnController {

    private final AvatarService avatarService;

    @MessageMapping("cdn.user.put.avatar")
    public Mono<Boolean> putUserAvatar(UpdateAvatar updateAvatar) {

        System.out.println(updateAvatar);
        return Mono.just(true);
    }

    //@PostMapping(value = "api/cdn/user/upload-avatar/{userid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    //@ResponseStatus(value = HttpStatus.OK)
    //public Flux<String> putUserInfoAvatar(@PathVariable String userid, @RequestPart("files") Flux<FilePart> filePartFlux) {
//
    //    return avatarService.getLines(filePartFlux);
    //    //return userServices.getUserByPathVarId(request)
    //    //        .flatMap(avatarService::fetchAvatarFromMPData)
    //    //        .flatMap(avatarService::validateAvatar)
    //    //        .flatMap(avatarService::updateUserAvatarAndResponse);
    //}


}
