package com.droukos.aedservice.model.user;

import com.droukos.aedservice.environment.enums.Availability;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.droukos.aedservice.environment.constants.Platforms.IOS;
import static com.droukos.aedservice.environment.constants.Platforms.WEB;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class AppState {
  boolean on;
  Integer status;

  public static AppState isOnline(UserRes user) {
    return new AppState(
            true,
            user.getAppState().getStatus()
    );
  }

  public static AppState isOffline(UserRes user) {
    return new AppState(
            false,
            user.getAppState().getStatus()
    );
  }

  public static AppState statusInvisible() {
    return new AppState(
            true,
            Availability.INVISIBLE.getCode()
    );
  }

  public static AppState statusAway() {
    return new AppState(
            true,
            Availability.AWAY.getCode()
    );
  }

  public static AppState statusOffline() {
    return new AppState(
            true,
            Availability.OFFLINE.getCode()
    );
  }

  public static AppState statusBusy() {
    return new AppState(
            true,
            Availability.BUSY.getCode()
    );
  }

  public static AppState statusOnDuty() {
    return new AppState(
            true,
            Availability.ON_DUTY.getCode()
    );
  }

  public static AppState statusOnline() {
    return new AppState(
            true,
            Availability.ONLINE.getCode()
    );
  }

  public static AppState caseOtherJwtModelsNullAppStateOffline(UserRes user, String logoutDevice) {
    return switch (logoutDevice) {
      case WEB -> user.getIosJwtModel() == null && user.getAndroidJwtModel() == null
              ? new AppState(false, user.getAppState().getStatus())
              : new AppState(true, user.getAppState().getStatus());
      case IOS -> user.getWebJwtModel() == null && user.getAndroidJwtModel() == null
              ? new AppState(false, user.getAppState().getStatus())
              : new AppState(true, user.getAppState().getStatus());
      default -> user.getWebJwtModel() == null && user.getIosJwtModel() == null
              ? new AppState(false, user.getAppState().getStatus())
              : new AppState(true, user.getAppState().getStatus());
    };
  }

}
