package com.cjh.ttt.base.token;

import com.cjh.ttt.entity.User;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户信息上下文，保存当前用户信息
 *
 * @author cjh
 * @date 2020/2/28
 */
@Slf4j
public class UserContext {

    /**
     * 当前线程的用户信息
     */
    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    /**
     * 功能：获取用户id
     */
    public static Integer getUserId() {
        return getUser().getId();
    }

    /**
     * 功能：获取用户getToken
     */
    public static String getToken() {
        return getUser().getToken();
    }

    /**
     * 功能：获取用户昵称
     */
    public static String getNickname() {
        return getUser().getNickname();
    }

    /**
     * 功能：获取用户姓名
     */
    public static String getUsername() {
        return getUser().getUsername();
    }

    /**
     * 功能：获取手机号
     */
    public static String getPhone() {
        return getUser().getPhone();
    }

    /**
     * 功能：获取用户信息
     */
    public static User getUser() {
        return userThreadLocal.get();
    }

    /**
     * 设置用户信息
     */
    public static void setUser(User user) {
        userThreadLocal.set(user);
    }

    /**
     * 清除数据
     */
    public static void clearUser() {
        userThreadLocal.remove();
    }
}
