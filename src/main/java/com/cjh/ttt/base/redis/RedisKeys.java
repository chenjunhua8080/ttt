package com.cjh.ttt.base.redis;

/**
 * redis keys
 *
 * @author cjh
 * @date 2020/3/10
 */
public class RedisKeys {

    public static String getTokenKey(String token) {
        return String.format("%s:%s-%s:%s", "ttt", "user", "token", token);
    }

    public static String getToutiaoTokenKey() {
        return String.format("%s:%s-%s", "ttt", "toutiao", "token");
    }

}
