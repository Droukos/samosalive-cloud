package com.droukos.authservice.util;

import com.droukos.authservice.model.user.UserRes;

public class DeviceDetector {
  private DeviceDetector() {}

  public static void detectUserDeviceOs(UserRes userRes) {
    String browserDetails = userRes.getServerRequest().headers().header("User-Agent").get(0);
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
