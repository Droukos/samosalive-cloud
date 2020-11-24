package com.droukos.cdnservice.service.cdn;

import com.droukos.cdnservice.environment.dto.server.AedDeviceImgsDto;
import com.droukos.cdnservice.model.aed_device.AedDevice;
import com.droukos.cdnservice.model.user.UserRes;
import com.droukos.cdnservice.repo.AedDeviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static com.droukos.cdnservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@AllArgsConstructor
public class AedDeviceService {

    private final AedDeviceRepository aedDeviceRepository;

    public Mono<AedDeviceImgsDto> fetchAddressAndDevicePicFromMPData(ServerRequest request) {
        return request.body(BodyExtractors.toMultipartData())
                .flatMap(parts -> Mono.just(parts.toSingleValueMap()))
                .flatMap(AedDeviceImgsDto::buildMonoFromMapPart);
    }

    public Mono<Tuple2<AedDevice, AedDeviceImgsDto>> validateImgsSize(Tuple2<AedDevice, AedDeviceImgsDto> tuple2) {
        AedDeviceImgsDto aedDeviceImgsDto = tuple2.getT2();

        long addrImgInMegabytes = aedDeviceImgsDto.getAddressImg().length() / 1024 / 1024;
        long devImgInMegabytes = aedDeviceImgsDto.getDeviceImg().length() / 1024 / 1024;
        if (addrImgInMegabytes > 1 || devImgInMegabytes > 1) {
            aedDeviceImgsDto.getAddressImg().delete();
            aedDeviceImgsDto.getDeviceImg().delete();
            return Mono.error(badRequest());
        }
        return Mono.just(tuple2);
    }

    public Mono<ServerResponse> saveAedDeviceImgs(AedDevice aedDevice) {
        return aedDeviceRepository.save(aedDevice)
                .then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue("Updated")));
    }
}
