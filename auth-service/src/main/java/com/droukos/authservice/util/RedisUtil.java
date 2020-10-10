package com.droukos.authservice.util;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.model.user.UserRes;

public class RedisUtil {

    private RedisUtil(){}

    public static String redisTokenK(UserRes user, NewAccTokenData accessTokenData) {
        return user.getId()+"-"+accessTokenData.getUserDevice();
    }
    public static String redisTokenK(UserRes user, String device) {
        return user.getId()+"-"+device;
    }
    public static String redisTokenK(String userid, String device) {
        return userid+"-"+device;
    }



}
