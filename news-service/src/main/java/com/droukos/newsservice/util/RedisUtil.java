package com.droukos.newsservice.util;

public class RedisUtil {

    private RedisUtil() {
    }

    public static String redisTokenK(String userid, String device) {
        return userid + "-" + device;
    }


}
