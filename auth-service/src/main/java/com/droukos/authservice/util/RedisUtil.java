package com.droukos.authservice.util;

import com.droukos.authservice.model.user.UserRes;

public class RedisUtil {

    private RedisUtil(){}

    public static String redisTokenK(UserRes user) {
        return user.getId()+"-"+user.getRequesterAccessTokenData().getUserDevice();
    }
    public static String redisTokenK(UserRes user, String device) {
        return user.getId()+"-"+device;
    }
    public static String redisTokenK(String userid, String device) {
        return userid+"-"+device;
    }



}
