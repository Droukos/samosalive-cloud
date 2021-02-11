package com.droukos.authservice.util;

import java.util.List;
import java.util.function.Function;

import static com.droukos.authservice.environment.constants.authorities.Roles.*;

public class RolesUtil {

    private static final Function<String, IllegalStateException> unexpectedValue =
            role -> new IllegalStateException("Unexpected value: " + role);

    private RolesUtil() {
    }

    public static int rolePower(String role) {
        return switch (clearRole(role)) {
            case GENERAL_ADMIN -> 0;
            case AREA_ADMIN -> 100;
            case USER -> 10000;
            default -> throw unexpectedValue.apply(role);
        };
    }

    public static String genreRole(String genreRole) {
        return switch (genreRole) {
            case "ADMINS" -> "0";
            case "USERS" -> "10000";
            default -> throw unexpectedValue.apply(genreRole);
        };
    }

    public static String roleCode(String role) {
        return switch (clearRole(role)) {
            case GENERAL_ADMIN -> "0.0";
            case AREA_ADMIN -> "0.100";
            case RESCUER -> "100.100";
            case TECHNICIAN -> "110.100";
            case USER -> "10000.10000";

            default -> throw unexpectedValue.apply(role);
        };
    }

    public static boolean doesRoleNotExist(String role) {
        return switch (clearRole(role)) {
            case GENERAL_ADMIN, AREA_ADMIN, USER, RESCUER, TECHNICIAN  -> false;
            default -> true;
        };
    }

    private static String clearRole(String role) {
        return role.replace("ROLE_", "");
    }

    public static boolean roleChangeValidAdmins(List<String> roles) {
        return roles.stream()
                .anyMatch(role -> switch (role) {
                    case GENERAL_ADMIN, AREA_ADMIN -> true;
                    default -> false;
                });
    }
}
