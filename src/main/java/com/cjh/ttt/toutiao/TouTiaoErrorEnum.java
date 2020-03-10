package com.cjh.ttt.toutiao;

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
public enum TouTiaoErrorEnum {

    /**
     * 头条异常枚举
     */
    ERROR_0(0, "请求成功"),
    ERROR_1(-1, "系统错误"),
    ERROR_40001(40001, "http 包体无法解析"),
    ERROR_40002(40002, "access_token 无效"),
    ERROR_40014(40014, "参数无效"),
    ERROR_40015(40015, "appid 错误"),
    ERROR_40017(40017, "secret 错误"),
    ERROR_40018(40018, "code 错误"),
    ERROR_40019(40019, "acode 错误"),
    ERROR_40020(40020, "grant_type 不是 client_credential"),
    ;

    private int code;
    private String name;

    public static TouTiaoErrorEnum from(Integer code) {
        if (code == null) {
            return null;
        }
        for (TouTiaoErrorEnum e : TouTiaoErrorEnum.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        TouTiaoErrorEnum e = from(code);
        return e == null ? "" : e.getName();
    }
}
