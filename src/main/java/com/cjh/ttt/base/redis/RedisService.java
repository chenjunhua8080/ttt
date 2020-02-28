package com.cjh.ttt.base.redis;

import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis缓存服务
 *
 * @author cjh
 * @date 2020/2/27
 */
@Component
@AllArgsConstructor
public class RedisService {

    private StringRedisTemplate stringRedisTemplate;

    public final static long DAY_3 = 60 * 60 * 24 * 3;
    public final static long DAY_1 = 60 * 60 * 24;
    public final static long HOUSE_1 = 60 * 60;
    public final static long NOT_EXPIRE = -1;

    public void set(String key, Object value, long expire) {
        stringRedisTemplate.opsForValue().set(key, value.toString());
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    public void set(String key, Object value) {
        set(key, value, DAY_1);
    }

    public void expire(String key, long expire) {
        stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    public <T> T get(String key) {
        Object value = stringRedisTemplate.opsForValue().get(key);
        return value == null ? null : (T) value;
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    public void convertAndSend(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }
}
