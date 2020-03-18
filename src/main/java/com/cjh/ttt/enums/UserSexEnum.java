package com.cjh.ttt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举
 *
 * @author cjh
 * @date 2020/2/19
 */
@Getter
@AllArgsConstructor
public enum UserSexEnum {

    /**
     * 性别枚举
     */
    UNKNOWN(0, "命中注定"),
    BOY(1, "专属推荐"),
    GIRL(2, "萍水相逢"),
    ;

    private int code;
    private String name;

    public static UserSexEnum from(Integer code) {
        if (code == null) {
            return null;
        }
        for (UserSexEnum e : UserSexEnum.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        UserSexEnum e = from(code);
        return e == null ? "" : e.getName();
    }
}
