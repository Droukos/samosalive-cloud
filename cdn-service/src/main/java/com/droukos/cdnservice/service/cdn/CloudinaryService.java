package com.droukos.cdnservice.service.cdn;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.droukos.cdnservice.config.CloudinaryConfigProperties;
import com.droukos.cdnservice.environment.dto.server.AedDeviceImgsDto;
import com.droukos.cdnservice.environment.dto.server.AvatarImgDto;
import com.droukos.cdnservice.model.aed_device.AedDevice;
import com.droukos.cdnservice.model.user.UserRes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.droukos.cdnservice.environment.security.HttpExceptionFactory.badRequest;

@Service
@AllArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private final CloudinaryConfigProperties cloudinaryConfigProperties;

    public Mono<Tuple2<UserRes, String>> uploadAvatarFile(Tuple2<UserRes, AvatarImgDto> tuple2) {
        String userid = tuple2.getT1().getId();
        File uploadedFile = tuple2.getT2().getAvatarFile();
        return Mono.fromCallable(() -> blockingAvatarUpload(userid, uploadedFile))
                .publishOn(Schedulers.elastic())
                .flatMap(url -> Mono.zip(Mono.just(tuple2.getT1()), Mono.just(url)));
    }

    public Mono<Tuple2<AedDevice, List<String>>> uploadAedDeviceFilePics(Tuple2<AedDevice, AedDeviceImgsDto> tuple2) {
        String deviceId = tuple2.getT1().getId();
        return Mono.fromCallable(() -> blockingAedDeviceImgsUpload(deviceId, tuple2.getT2()))
                .publishOn(Schedulers.elastic())
                .flatMap(urls -> Mono.zip(Mono.just(tuple2.getT1()), Mono.just(urls)));
    }

    public List<String> blockingAedDeviceImgsUpload(String deviceId, AedDeviceImgsDto aedDeviceImgsDto) {
        try {
            List<String> urls = new ArrayList<>();

            urls.add(cloudinary.uploader().upload(aedDeviceImgsDto.getDeviceImg(),
                    ObjectUtils.asMap(
                            "public_id", cloudinaryConfigProperties.getCloudFolderDevice() + deviceId,
                            "overwrite", true))
                    .get("url")
                    .toString());
            urls.add(cloudinary.uploader().upload(aedDeviceImgsDto.getAddressImg(),
                    ObjectUtils.asMap(
                            "public_id", cloudinaryConfigProperties.getCloudFolderAddress() + deviceId,
                            "overwrite", true))
                    .get("url")
                    .toString());

            return urls;
        } catch (IOException e) {
            e.printStackTrace();
            throw badRequest();
        } finally {
            aedDeviceImgsDto.getAddressImg().delete();
            aedDeviceImgsDto.getDeviceImg().delete();
        }
    }

    private String blockingAvatarUpload(String userid, File uploadedFile) {
        try {
            return cloudinary.uploader().upload(uploadedFile,
                    ObjectUtils.asMap(
                            "public_id", cloudinaryConfigProperties.getCloudFolderAvatars() + userid,
                            "overwrite", true))
                    .get("url")
                    .toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw badRequest();
        } finally {
            uploadedFile.delete();
        }
    }
}
