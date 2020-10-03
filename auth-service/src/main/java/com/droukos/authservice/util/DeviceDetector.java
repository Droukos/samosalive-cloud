package com.droukos.authservice.util;

import com.droukos.authservice.model.user.UserRes;

import java.util.List;

public class DeviceDetector {
  private DeviceDetector() {}

  public static void detectUserDeviceOs(UserRes userRes) {
    List<String> userAgent = userRes.getServerRequest().headers().header("User-Agent");
    
    if (userAgent.isEmpty()) {
      userRes.setUserDevice("web");
      return;
    }

    String browserDetails = userAgent.get(0);
    String user = browserDetails.toLowerCase();

    String os;
    if (user.contains("android")) {
      os = "android";
    } else if (user.contains("iphone")) {
      os = "ios";
    } else {
      os = "web";
    }
    userRes.setUserDevice(os);
  }
}
