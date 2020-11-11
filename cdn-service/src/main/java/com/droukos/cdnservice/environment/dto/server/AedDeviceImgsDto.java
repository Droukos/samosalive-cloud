package com.droukos.cdnservice.environment.dto.server;

import com.droukos.cdnservice.util.FilesUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.client.HttpServerErrorException;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.Map;
import java.util.stream.Collectors;

import static com.droukos.cdnservice.environment.security.HttpExceptionFactory.badRequest;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AedDeviceImgsDto {
    private File addressImg;
    private File deviceImg;

    public static Mono<AedDeviceImgsDto> buildMonoFromMapPart(Map<String, Part> partMap) {
        return Mono.just(buildDtoFromMapPart(partMap));
    }

    public static AedDeviceImgsDto buildDtoFromMapPart(Map<String, Part> partMap) {
        FilePart f = (FilePart) partMap.get("addrFile");
        FilePart f1 = (FilePart) partMap.get("deviceFile");
        if(f == null || f1 == null) throw badRequest();
        File addressFile = new File(FilesUtil.tempPathForAedAddressPic() + f.filename());
        File deviceFile = new File(FilesUtil.tempPathForAedDevicePic() + f1.filename());

        f.transferTo(addressFile);
        f1.transferTo(deviceFile);
        return new AedDeviceImgsDto(
                addressFile,
                deviceFile
        );
    }
}
