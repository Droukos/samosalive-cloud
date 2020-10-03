package com.droukos.authservice.environment.services.sec_options;

import com.droukos.authservice.environment.interfaces.core_services.SecRunByInfo;
import com.droukos.authservice.util.RolesUtil;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.droukos.authservice.environment.constants.authorities.Roles.AREA_ADMIN;
import static com.droukos.authservice.environment.constants.authorities.Roles.GENERAL_ADMIN;
import static com.droukos.authservice.environment.services.GeneralSemantics.ANY;
import static com.droukos.authservice.util.RolesUtil.genreRole;

/**
 * putFromSameUserId -> The very same user that owns the resource, is trying to update his resource.
 * Things will go wrong if he's trying to update his role, only an admin shall do that.
 */
@AllArgsConstructor
public enum GeneralSecOptions implements SecRunByInfo {
  SAME_USER_ONLY(null, true),
  NO_RUN_BY_SEC(null, false),
  ANY_USER(Collections.singletonList("*.*"), false),
  SAME_USER_OR_ANY_ADMIN(Collections.singletonList("0.*"), true),
  SAME_USER_OR_GENERAL_ADMIN(generalAdmin(), true),
  ADMINS_RESCUER(Arrays.asList("0.*", "1000.*"), false),
  SAME_USER_OR_AREA_ADMIN(areaAdmin(), true),
  ONLY_GENERAL_ADMIN(generalAdmin(), false),
  ONLY_AREA_ADMIN(areaAdmin(), false),
  ONLY_ANY_ADMIN(anyAdmin(), false);

  private final List<String> codes;
  private final boolean putFromSameUserId;

  private static List<String> anyAdmin() {
    return Collections.singletonList(genreRole("ADMINS") + ANY.getCode());
  }

  private static List<String> generalAdmin() {
    return Collections.singletonList(RolesUtil.roleCode(GENERAL_ADMIN));
  }

  private static List<String> areaAdmin() {
    return Collections.singletonList(RolesUtil.roleCode(AREA_ADMIN));
  }

  public List<String> getCodes() {
    return codes;
  }

  public boolean getPutFromSameUserId() {
    return putFromSameUserId;
  }
}
