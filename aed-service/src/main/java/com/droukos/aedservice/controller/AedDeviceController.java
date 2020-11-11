package com.droukos.aedservice.controller;

import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceIdDto;
import com.droukos.aedservice.environment.dto.client.aed_device.AedDeviceRegisterDto;
import com.droukos.aedservice.environment.dto.server.aed.aed_device.AedDeviceInfoDto;
import com.droukos.aedservice.model.factories.AedDeviceFactory;
import com.droukos.aedservice.service.aed_device.AedDeviceInfo;
import com.droukos.aedservice.service.aed_device.AedDeviceRegister;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class AedDeviceController {

  private final AedDeviceRegister aedDeviceRegister;
  private final AedDeviceInfo aedDeviceInfo;

  @MessageMapping("aed.register.device")
  public Mono<Boolean> registerDevice(AedDeviceRegisterDto aedDeviceRegisterDto) {

    return Mono.just(aedDeviceRegisterDto)
        .flatMap(aedDeviceRegister::validateRegisterDto)
        .zipWith(ReactiveSecurityContextHolder.getContext())
        .flatMap(AedDeviceFactory::buildDeviceMono)
        .flatMap(aedDeviceRegister::aedDeviceToMongoDB)
        .then(Mono.just(true));
  }

  public Mono<AedDeviceInfoDto> fetchDeviceInfo(AedDeviceIdDto aedDeviceIdDto) {

    return Mono.just(aedDeviceIdDto)
        .flatMap(aedDeviceInfo::fetchAedDeviceById)
        .flatMap(AedDeviceInfoDto::buildMono);
  }

  public void fetchDevicesInArea() {

  }

  public void transferDeviceToNextEvent() {}

  public void takeDeviceForEvent() {}

  public void returnDevice() {}
}
