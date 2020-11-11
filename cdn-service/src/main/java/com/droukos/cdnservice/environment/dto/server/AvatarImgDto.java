package com.droukos.cdnservice.environment.dto.server;

import com.droukos.cdnservice.util.FilesUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.Map;

import static com.droukos.cdnservice.environment.security.HttpExceptionFactory.badRequest;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AvatarImgDto {
    private File avatarFile;

    public static Mono<AvatarImgDto> buildMonoFromPart(Map<String, Part> partMap) {
        return Mono.just(buildFromPart(partMap));
    }

    public static AvatarImgDto buildFromPart(Map<String, Part> partMap) {
        FilePart f = (FilePart) partMap.get("avFile");
        if(f == null) throw badRequest();
        File avatarFile = new File(FilesUtil.tempPathForAvatarPic() + f.filename());

        f.transferTo(avatarFile);
        return new AvatarImgDto(avatarFile);
    }

}
