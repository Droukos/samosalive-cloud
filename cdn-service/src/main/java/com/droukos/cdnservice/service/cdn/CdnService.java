package com.droukos.cdnservice.service.cdn;

import com.droukos.cdnservice.model.aed_device.AedDevice;
import com.droukos.cdnservice.model.user.UserRes;
import com.droukos.cdnservice.repo.AedDeviceRepository;
import com.droukos.cdnservice.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import static com.droukos.cdnservice.util.factories.HttpExceptionFactory.badRequest;

@Service
@AllArgsConstructor
public class CdnService {
    private final AedDeviceRepository aedDeviceRepository;
    private final UserRepository userRepository;

    public Mono<AedDevice> fetchAedDeviceFromRequest(ServerRequest request) {
        String deviceId = request.pathVariable("id");
        return deviceId.trim().equals("") ? Mono.error(badRequest()) : fetchAedDeviceById(deviceId);
    }

    public Mono<UserRes> fetchUserFromRequest(ServerRequest request) {
        String userId = request.pathVariable("id");
        return userId.trim().equals("") ? Mono.error(badRequest()) : userRepository.findFirstById(userId);
    }

    public Mono<AedDevice> fetchAedDeviceById(String deviceId) {
        return aedDeviceRepository
                .getAedDeviceById(deviceId)
                .flatMap(
                        aedDevice ->
                                aedDevice.getId() == null
                                        ? Mono.error(badRequest("Device not found"))
                                        : Mono.just(aedDevice));
    }
}
