package com.droukos.aedservice.controller;

import com.droukos.aedservice.environment.dto.client.aed_device.*;
import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceInfoDto;
import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceInfoPreviewDto;
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

    @MessageMapping("aed.device.fetch.inArea")
    public Flux<AedDeviceInfoPreviewDto> fetchDevicesInArea(AedDeviceAreaSearchDto aedDeviceAreaSearchDto) {

        return Flux.just(aedDeviceAreaSearchDto)
                .flatMap(aedDeviceInfo::validateAedDeviceMaxDistance)
                .flatMap(aedDeviceInfo::findAedDeviceInArea)
                .flatMap(AedDeviceInfoPreviewDto::buildMono);
    }

    public Mono<Boolean> transferDeviceToNextEvent(AedDeviceTransferToNextEvent dto) {

        return Mono.empty();
        //return Mono.just(dto)
        //        .
    }

    public Mono<Boolean> returnDeviceFromEvent(AedDeviceReturnDto dto) {

        return Mono.empty();
        //return Mono.just(dto)
        //        .
    }

    public Mono<Boolean> returnDeviceFromRepair(AedDeviceRegisterDto dto) {

        return Mono.empty();
    }

    public Mono<Boolean> takeDeviceForEvent(AedDeviceForEventDto dto) {

        return Mono.zip(Mono.just(dto), ReactiveSecurityContextHolder.getContext())
                .flatMap(aedDeviceEvent::validateAdminOrSameUser)
                .then(Mono.zip(
                        Mono.just(dto),
                        aedDeviceEvent.fetchAedDeviceById(dto),
                        aedDeviceEvent.fetchAedEventById(dto)
                        )
                )
                .flatMap(aedDeviceEvent::validateAedEventDeviceSub)
                .flatMap(AedDeviceEventFactory::buildZippedAedDeviceAndEvent)
                .flatMap(aedDeviceEvent::saveAedDeviceAndEvent);
    }


}
