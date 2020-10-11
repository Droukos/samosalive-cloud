package com.droukos.userservice.util;

import com.droukos.userservice.model.user.UserRes;

public class RedisUtil {

    private RedisUtil(){}

    public static String redisTokenK(UserRes user, String device) {
        return user.getId()+"-"+device;
    }
    public static String redisTokenK(String userid, String device) {
        return userid+"-"+device;
    }



}
