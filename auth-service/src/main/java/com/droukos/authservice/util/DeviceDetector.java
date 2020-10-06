package com.droukos.authservice.util;

import com.droukos.authservice.environment.dto.RequesterAccessTokenData;
import com.droukos.authservice.environment.dto.RequesterRefreshTokenData;
import com.droukos.authservice.model.user.UserRes;

import java.util.List;

public class DeviceDetector {
  private DeviceDetector() {}

  public static void detectUserDeviceOs(UserRes userRes) {
    List<String> userAgent = userRes.getServerRequest().headers().header("User-Agent");
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
    userRes.setRequesterAccessTokenData(RequesterAccessTokenData.builder().userDevice(os).build());
    userRes.setRequesterRefreshTokenData(RequesterRefreshTokenData.builder().userDevice(os).build());
  }
}
