package com.cjh.ttt.base.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常枚举
 *
 * @author cjh
 * @date 2020/2/19
 */
@Getter
@AllArgsConstructor
public enum ErrorEnum {

    /**
     * 异常枚举
     */
    ERROR_500(500, "服务器异常"),
    ERROR_404(404, "api不存在"),
    ERROR_412(412, "没有权限"),
    ERROR_413(413, "参数错误"),
    TOKEN_NULL(10001, "token 不能为空"),
    TOKEN_EXPIRE(10002, "token 已过期"),
    PHONE_BIND_ED(10003, "手机号已绑定其他用户"),
    SEX_NOT_SET(10004, "性别还未设置哦"),
    BIRTHDAY_NOT_SET(10005, "生日还未设置哦"),
    PAIR_SEND_ED(10006, "等待对方确定"),
    PAIR_SUCCESS_ED(10007, "已配对成功"),
    MAP_ERROR(10008, "地图服务调用失败"),
    ADDRESS_NOT_SET(10009, "请先设置定位"),
    NOT_PAIR(10010, "请先完成配对"),
    RELIEVE_PAIR(10011, "对方已解除配对"),
    NICKNAME_LENGTH_10(10012, "昵称限制10个字符"),
    ;

    private int code;
    private String name;

    public static ErrorEnum from(Integer code) {
        if (code == null) {
            return null;
        }
        for (ErrorEnum e : ErrorEnum.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        ErrorEnum e = from(code);
        return e == null ? "" : e.getName();
    }
}
