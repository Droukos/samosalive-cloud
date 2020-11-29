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
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AedDeviceImgAddressDto {
    private File addressImg;

    public static Mono<AedDeviceImgAddressDto> buildMonoFromMapPart(Map<String, Part> partMap) {
        return Mono.just(buildDtoFromMapPart(partMap));
    }

    public static AedDeviceImgAddressDto buildDtoFromMapPart(Map<String, Part> partMap) {
        FilePart f = (FilePart) partMap.get("addrFile");
        if (f == null) throw badRequest();
        File addressFile = new File(FilesUtil.tempPathForAedAddressPic() + f.filename());

        f.transferTo(addressFile);
        return new AedDeviceImgAddressDto(
                addressFile
        );
    }
}
