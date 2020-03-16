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
    TOKEN_NULL(10001, "token 不能为空"),
    TOKEN_EXPIRE(10002, "token 已过期"),
    PHONE_BIND_ED(10003, "手机号已绑定其他用户"),
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
