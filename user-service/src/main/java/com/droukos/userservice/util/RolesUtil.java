package com.droukos.userservice.util;

import org.springframework.security.core.parameters.P;

import java.util.List;

import static com.droukos.userservice.environment.constants.authorities.Roles.*;

public class RolesUtil {

  private RolesUtil() {}

  public static int rolePower(String role) {
      return switch (clearRole(role)) {
          case GENERAL_ADMIN -> 0;
          case AREA_ADMIN -> 100;
          case USER -> 10000;
          default -> throw new IllegalStateException("Unexpected value: " + role);
      };
  }

  public static boolean hasAnyAdminRole(List<String> roles) {
      return roles.stream().anyMatch(RolesUtil::isAnyAdmin);
  }

  public static boolean isAnyAdmin(String role) {
      return switch (role) {
          case GENERAL_ADMIN, AREA_ADMIN -> true;
          default -> false;
      };
  }

  public static String genreRole(String genreRole) {
      return switch (genreRole) {
          case "ADMINS" -> "0";
          case "USERS" -> "10000";
          default -> throw new IllegalStateException("Unexpected value: " + genreRole);
      };
  }
  public static String roleCode(String role) {
      return switch (clearRole(role)) {
          case GENERAL_ADMIN -> "0.0";
          case AREA_ADMIN -> "0.100";
          case USER -> "10000.10000";

          default -> throw new IllegalStateException("Unexpected value: " + role);
      };
  }

  public static boolean doesRoleNotExist(String role) {
      return switch (clearRole(role)) {
        case GENERAL_ADMIN, AREA_ADMIN, USER -> false;
        default -> true;
      };
  }

    private static String clearRole(String role){
        return role.replace("ROLE_","");
    }
}
