package com.droukos.cdnservice.service.cdn;

import com.droukos.cdnservice.environment.dto.server.AvatarImgDto;
import com.droukos.cdnservice.model.user.UserRes;
import com.droukos.cdnservice.repo.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.droukos.cdnservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@RequiredArgsConstructor
public class AvatarService {

    @NonNull
    private final UserRepository userRepository;

    public Mono<AvatarImgDto> fetchAvatarPicFromMPData(ServerRequest request) {
        //return request.body(BodyExtractors.toParts())
        //        .cast(FilePart.class)
        return request.multipartData()
                .flatMap(parts -> Mono.just(parts.toSingleValueMap()))
        //return request.body(BodyExtractors.toMultipartData())
        //        .flatMap(parts -> Mono.just(parts.toSingleValueMap()))
                .flatMap(AvatarImgDto::buildMonoFromPart);
    }

    public Mono<Tuple2<UserRes, AvatarImgDto>> validateImgsSize(Tuple2<UserRes, AvatarImgDto> tuple2) {
        AvatarImgDto avatarImgDto = tuple2.getT2();

        long avatarImgSize = avatarImgDto.getAvatarFile().length() / 1024 / 1024;
        if (avatarImgSize > 1) {
            avatarImgDto.getAvatarFile().delete();
            return Mono.error(badRequest());
        }
        return Mono.just(tuple2);
    }

    public Mono<ServerResponse> saveUserAvatar(UserRes user) {
        return userRepository.save(user)
                .then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue("Updated")));
    }

    // public Mono<UpdateAvatar> validateAvatar(UpdateAvatar avatar) {
    //    Mono.just(ImageIO.read(avatar.getAv()).toString())
    //    .on;
    //  return Mono.just(avatar);
    // }
    //
    // public Mono<UpdateAvatar> fetchAvatarFromMPData(User user) {
    //  return request
    //      .body(BodyExtractors.toMultipartData())
    //      .flatMap(parts -> {
    //        FilePart filePart = (FilePart) parts.toSingleValueMap().get("file");
    //        File directory = new File(System.getProperty("user.dir") + "\\tmp\\");
    //        if (!directory.exists()) {
    //          directory.mkdir();
    //        }
    //        File f = new File(filePart.filename());
    //        filePart.transferTo(f);
    //        return Mono.just(new UpdateAvatar(user, f));
    //      });
    // }

    // public Mono<ServerResponse> updateUserAvatarAndResponse(UpdateAvatar update_avatar) {
    //  String avatarUrl = uploadFile(update_avatar.getUser().getId(), update_avatar.getAv());
    //
    //  Function<User, Mono<User>> beforeUpdate = userToUpdate -> {
    //    update_avatar.getUser().setAvatar(avatarUrl);
    //    return Mono.just(userToUpdate);
    //  };
    //
    //  Function<User, Mono<ServerResponse>> result = savedUser -> ok().body(BodyInserters.fromValue(
    //      new ApiResponse(StatusCodes.OK, avatarUrl, "edit.avatar_updated")));
    //
    //  return beforeUpdate.apply(update_avatar.getUser())
    //      .flatMap(userRepository::save)
    //      .flatMap(result);
    // }

    // public String uploadFile(String userid, File uploadedFile) {
    //  try {
    //    //Map uploadResult = cloudinaryConfig.uploader().upload(uploadedFile, ObjectUtils.asMap(
    //    //        "public_id", cloudFolderAvatars + userid,
    //    //        "overwrite", true
    //    //));
    //    uploadedFile.delete();
    //
    //    //return uploadResult.get("url").toString();
    //    return "";
    //  } catch (Exception e) {
    //    throw new RuntimeException(e);
    //  }
    // }

    //public Flux<String> getLines(Flux<FilePart> filePartMono) {
    //    return filePartMono.flatMap(
    //            filePart ->
    //                    filePart
    //                            .content()
    //                            .map(
    //                                    dataBuffer -> {
    //                                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
    //                                        dataBuffer.read(bytes);
    //                                        DataBufferUtils.release(dataBuffer);
    //                                        return new String(bytes, StandardCharsets.UTF_8);
    //                                    })
    //                            .map(this::processAndGetLinesAsList)
    //                            .flatMapIterable(Function.identity()));
    //}

    //private List<String> processAndGetLinesAsList(String string) {
    //    Supplier<Stream<String>> streamSupplier = string::lines;
    //    System.out.println(string);
    //    return streamSupplier.get().filter(s -> !s.isBlank()).collect(Collectors.toList());
    //    // var isFileOk = streamSupplier.get().allMatch(line ->
    //    //        line.matches(MultipartFileUploadUtils.REGEX_RULES));
    //    // return isFileOk ? streamSupplier.get()
    //    //        .filter(s -> !s.isBlank())
    //    //        .collect(Collectors.toList())
    //    //            : new ArrayList<>();
    //}
}
