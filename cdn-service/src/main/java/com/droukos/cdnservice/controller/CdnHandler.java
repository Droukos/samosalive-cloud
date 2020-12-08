package com.droukos.cdnservice.controller;

import com.droukos.cdnservice.model.factories.AedDevicePicsFactory;
import com.droukos.cdnservice.model.factories.AvatarForUserFactory;
import com.droukos.cdnservice.service.cdn.AedDeviceService;
import com.droukos.cdnservice.service.cdn.AvatarService;
import com.droukos.cdnservice.service.cdn.CdnService;
import com.droukos.cdnservice.service.cdn.CloudinaryService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@AllArgsConstructor
public class CdnHandler {

    private final CdnService cdnService;
    private final AedDeviceService aedDeviceService;
    private final CloudinaryService cloudinaryService;
    private final AvatarService avatarService;


    @PutMapping(value = "api/cdn/aedDevice/upload-device-pics/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Flux<String> upload(@RequestPart("files") Flux<FilePart> filePartFlux, @PathVariable String id) {
        return filePartFlux.flatMap(filePart ->
                filePart.content().map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return new String(bytes, StandardCharsets.UTF_8);
                })
                        .map(this::processAndGetLinesAsList)
                        .flatMapIterable(Function.identity()));
        //return uploadService.getLines(filePartFlux);
    }


    private List<String> processAndGetLinesAsList(String string) {
        Supplier<Stream<String>> streamSupplier = string::lines;
        //var isFileOk = streamSupplier.get().allMatch(line ->
        //        line.matches(MultipartFileUploadUtils.REGEX_RULES));
        return streamSupplier.get().collect(Collectors.toList());
        //return isFileOk ? streamSupplier.get()
        //        .filter(s -> !s.isBlank())
        //        .collect(Collectors.toList())
        //            : new ArrayList<>();
    }

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
                .flatMap(aedDeviceService::saveAedDevice);
    }

    public Mono<ServerResponse> putNewAedDevicePic(ServerRequest request) {
        return cdnService.fetchAedDeviceFromRequest(request)
                .zipWith(aedDeviceService.fetchDevicePicFromMPData(request))
                .flatMap(aedDeviceService::validateDeviceImgSize)
                .flatMap(cloudinaryService::uplopadAedDevicePic)
                .flatMap(AedDevicePicsFactory::updateDevicePicUrlMono)
                .flatMap(aedDeviceService::saveAedDevice);
    }

    public Mono<ServerResponse> putNewAedDeviceAddressPic(ServerRequest request) {
        return cdnService.fetchAedDeviceFromRequest(request)
                .zipWith(aedDeviceService.fetchAddressPicFromMPData(request))
                .flatMap(aedDeviceService::validateAddressImgSize)
                .flatMap(cloudinaryService::uploadAedDeviceAddressPic)
                .flatMap(AedDevicePicsFactory::updateDeviceAddressPicUrlMono)
                .flatMap(aedDeviceService::saveAedDevice);
    }
}
