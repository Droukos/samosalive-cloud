package com.droukos.cdnservice.controller;

import com.droukos.cdnservice.model.factories.AedDevicePicsFactory;
import com.droukos.cdnservice.model.factories.AvatarForUserFactory;
import com.droukos.cdnservice.model.user.UserRes;
import com.droukos.cdnservice.service.cdn.AedDeviceService;
import com.droukos.cdnservice.service.cdn.AvatarService;
import com.droukos.cdnservice.service.cdn.CdnService;
import com.droukos.cdnservice.service.cdn.CloudinaryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class CdnHandler {

    private final CdnService cdnService;
    private final AedDeviceService aedDeviceService;
    private final CloudinaryService cloudinaryService;
    private final AvatarService avatarService;

    public Mono<ServerResponse> putNewAvatarPic(ServerRequest request) {
        return cdnService.fetchUserFromRequest(request)
                .zipWith(avatarService.fetchAvatarPicFromMPData(request))
                .flatMap(avatarService::validateImgsSize)
                .flatMap(cloudinaryService::uploadAvatarFile)
                .flatMap(AvatarForUserFactory::updateUserAvatarUrlMono)
                .flatMap(avatarService::saveUserAvatar);
    }
    public Mono<ServerResponse> putNewAedDevicePics(ServerRequest request) {
        return cdnService.fetchAedDeviceFromRequest(request)
                .zipWith(aedDeviceService.fetchAddressAndDevicePicFromMPData(request))
                .flatMap(aedDeviceService::validateImgsSize)
                .flatMap(cloudinaryService::uploadAedDeviceFilePics)
                .flatMap(AedDevicePicsFactory::updateDevicePicsUrlMono)
                .flatMap(aedDeviceService::saveAedDeviceImgs);
    }
}
