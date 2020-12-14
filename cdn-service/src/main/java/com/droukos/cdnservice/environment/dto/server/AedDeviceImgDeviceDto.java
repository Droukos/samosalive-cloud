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

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AedDeviceImgDeviceDto {
    private File deviceImg;

    public static Mono<AedDeviceImgDeviceDto> buildMonoFromMapPart(Map<String, Part> partMap) {
        return Mono.just(buildDtoFromMapPart(partMap));
    }

    public static AedDeviceImgDeviceDto buildDtoFromMapPart(Map<String, Part> partMap) {
        FilePart f = (FilePart) partMap.get("deviceFile");
        if (f == null ) throw badRequest();
        File deviceFile = new File(FilesUtil.tempPathForAedDevicePic() + f.filename());

        f.transferTo(deviceFile);
        return new AedDeviceImgDeviceDto(
                deviceFile
        );
    }
}
