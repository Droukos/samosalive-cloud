package com.droukos.authservice.util;

import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;

public class DeviceDetector {
  private DeviceDetector() {}

  public static String detectUserDeviceOs(ServerRequest request) {
    List<String> userAgent = request.headers().header("User-Agent");
    String os;

    if (userAgent.isEmpty()) {
      os = "web";
    } else {

      String browserDetails = userAgent.get(0);
      String user = browserDetails.toLowerCase();

      if (user.contains("android")) {
        os = "android";
      } else if (user.contains("iphone")) {
        os = "ios";
      } else {
        os = "web";
      }
    }
    return os;
  }
}
