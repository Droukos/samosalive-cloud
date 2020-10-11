package com.droukos.userservice.util;

import com.droukos.userservice.environment.dto.RequesterAccessTokenData;
import org.springframework.security.core.context.SecurityContext;

import java.util.List;

public class SecurityUtil {
    private SecurityUtil() {}

    public static RequesterAccessTokenData getRequesterData(SecurityContext context) {
        return (RequesterAccessTokenData) context.getAuthentication().getPrincipal();
    }

    public static String getRequesterUserId(SecurityContext context) {
        return ((RequesterAccessTokenData) context.getAuthentication().getPrincipal()).getUserId();
    }

    public static String getRequesterTokenId(SecurityContext context) {
        return ((RequesterAccessTokenData) context.getAuthentication().getPrincipal()).getTokenId();
    }

    public static String getRequesterUsername(SecurityContext context) {
        return ((RequesterAccessTokenData) context.getAuthentication().getPrincipal()).getUsername();
    }

    public static String getRequesterDevice(SecurityContext context) {
        return ((RequesterAccessTokenData) context.getAuthentication().getPrincipal()).getUserDevice();
    }

    public static List<String> getRequesterRoles(SecurityContext context) {
        return ((RequesterAccessTokenData) context.getAuthentication().getPrincipal()).getRoles();
    }
}
