package com.droukos.userservice.service.user;

import static com.droukos.userservice.util.ValidatorUtil.validate;

import com.droukos.userservice.environment.constants.StatusCodes;
import com.droukos.userservice.environment.dto.client.user.UpdateAvatar;
import com.droukos.userservice.environment.dto.server.ApiResponse;
import com.droukos.userservice.model.user.UserRes;
import com.droukos.userservice.repo.UserRepository;
import com.droukos.userservice.service.validator.user.AvatarValidator;
import java.io.File;
import java.util.function.Function;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
@Lazy
@RequiredArgsConstructor
public class AvatarService {

  @NonNull private final UserRepository userRepository;
  //private final CloudinaryService cloudinaryService;
  //private final Cloudinary cloudinaryConfig;

  //@Value("${cloudinary.folder.avatars}")
  //private final String cloudFolderAvatars;

  /*public Mono<UpdateAvatar> validateAvatar(UpdateAvatar avatar) {
    validate(avatar, AvatarValidator.build());
    return Mono.just(avatar);
  }

  public Mono<UpdateAvatar> fetchAvatarFromMPData(User user) {
    return request
        .body(BodyExtractors.toMultipartData())
        .flatMap(parts -> {
          FilePart filePart = (FilePart) parts.toSingleValueMap().get("file");
          File directory = new File(System.getProperty("user.dir") + "\\tmp\\");
          if (!directory.exists()) {
            directory.mkdir();
          }
          File f = new File(filePart.filename());
          filePart.transferTo(f);
          return Mono.just(new UpdateAvatar(user, f));
        });
  }

  public Mono<ServerResponse> updateUserAvatarAndResponse(UpdateAvatar update_avatar) {
    String avatarUrl = uploadFile(update_avatar.getUser().getId(), update_avatar.getAv());

    Function<User, Mono<User>> beforeUpdate = userToUpdate -> {
      update_avatar.getUser().setAvatar(avatarUrl);
      return Mono.just(userToUpdate);
    };

    Function<User, Mono<ServerResponse>> result = savedUser -> ok().body(BodyInserters.fromValue(
        new ApiResponse(StatusCodes.OK, avatarUrl, "edit.avatar_updated")));

    return beforeUpdate.apply(update_avatar.getUser())
        .flatMap(userRepository::save)
        .flatMap(result);
  }

  public String uploadFile(String userid, File uploadedFile) {
    try {
      //Map uploadResult = cloudinaryConfig.uploader().upload(uploadedFile, ObjectUtils.asMap(
      //        "public_id", cloudFolderAvatars + userid,
      //        "overwrite", true
      //));
      uploadedFile.delete();

      //return uploadResult.get("url").toString();
      return "";
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }*/
}
