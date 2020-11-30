package com.droukos.aedservice.controller;

import com.droukos.aedservice.environment.dto.client.aed_device.*;
import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceInfoDto;
import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceInfoPreviewDto;
import com.droukos.aedservice.model.aed_device.AedDevice;
import com.droukos.aedservice.model.factories.aed_device.AedDeviceEventFactory;
import com.droukos.aedservice.model.factories.aed_device.AedDeviceFactory;
import com.droukos.aedservice.service.aed_device.AedDeviceEvent;
import com.droukos.aedservice.service.aed_device.AedDeviceInfo;
import com.droukos.aedservice.service.aed_device.AedDeviceRegister;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class AedDeviceController {

    private final AedDeviceRegister aedDeviceRegister;
    private final AedDeviceInfo aedDeviceInfo;
    private final AedDeviceEvent aedDeviceEvent;

    @MessageMapping("aed.device.register")
    public Mono<String> registerDevice(AedDeviceRegisterDto aedDeviceRegisterDto) {

        return Mono.just(aedDeviceRegisterDto)
                .flatMap(aedDeviceRegister::validateRegisterDto)
                .zipWith(ReactiveSecurityContextHolder.getContext())
                .flatMap(aedDeviceRegister::validateUniqueNickname)
                .flatMap(AedDeviceFactory::buildDeviceMono)
                .flatMap(aedDeviceRegister::aedDeviceToMongoDB)
                .flatMap(aedDevice -> Mono.just(aedDevice.getId()));
    }

    @MessageMapping("aed.device.fetch.byId")
    public Mono<AedDeviceInfoDto> fetchDeviceInfoById(AedDeviceIdDto aedDeviceIdDto) {

        return Mono.just(aedDeviceIdDto)
                .flatMap(aedDeviceInfo::fetchAedDeviceById)
                .flatMap(AedDeviceInfoDto::buildMono);
    }

    @MessageMapping("aed.device.fetch.preview.byNickname")
    public Flux<AedDeviceInfoPreviewDto> fetchDeviceInfoPreviewByNickname(AedDeviceNicknameDto aedDeviceNicknameDto) {

        return Flux.just(aedDeviceNicknameDto)
                .flatMap(aedDeviceInfo::fetchAedDevicesByNickname)
                .flatMap(AedDeviceInfoPreviewDto::buildMono);
    }

    @MessageMapping("aed.device.info.edit")
    public Mono<Boolean> editDeviceInfo(AedDeviceEditDto aedDeviceEditDto) {

        return Mono.just(aedDeviceEditDto)
                .flatMap(aedDeviceInfo::validateAedDeviceEdit)
                .zipWith(aedDeviceInfo.fetchAedDeviceById(aedDeviceEditDto.getId()))
                .flatMap(AedDeviceFactory::buildEditedDeviceMono)
                .flatMap(aedDeviceInfo::saveAedDevice)
                .then(Mono.just(true));
    }

    public Mono<Boolean> updateDeviceStatus() {

        return Mono.empty();
    }

    @MessageMapping("aed.device.fetch.inArea")
    public Flux<AedDevice> fetchDevicesInArea(AedDeviceAreaSearchDto aedDeviceAreaSearchDto) {

        return Flux.just(aedDeviceAreaSearchDto)
                .flatMap(aedDeviceInfo::validateAedDeviceMaxDistance)
                .flatMap(aedDeviceInfo::findAedDeviceInArea);
    }

    public void transferDeviceToNextEvent() {

    }

    public Mono<Boolean> takeDeviceForEvent(AedDeviceForEventDto dto) {

        return Mono.just(dto)
                .zipWith(aedDeviceEvent.fetchAedDeviceById(dto))
                .flatMap(aedDeviceEvent::validateAedDeviceStatus)
                .flatMap(AedDeviceEventFactory::buildAedDeviceEventMono)
                .flatMap(aedDeviceEvent::saveAedDeviceOnEvent)
                .then(Mono.just(true));
    }

    public void returnDevice() {
    }
}
