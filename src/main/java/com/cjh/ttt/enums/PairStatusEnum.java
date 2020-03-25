package com.cjh.ttt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 配对状态枚举
 *
 * @author cjh
 * @date 2020/2/19
 */
@Getter
@AllArgsConstructor
public enum PairStatusEnum {

    /**
     * 配对状态枚举
     */
    WAIT(0, "已送达"),
    SUCCESS(1, "成功"),
    FAIL(2, "失败"),
    RELIEVE(3, "已解除"),
    ;

    private int code;
    private String name;

    public static PairStatusEnum from(Integer code) {
        if (code == null) {
            return null;
        }
        for (PairStatusEnum e : PairStatusEnum.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        PairStatusEnum e = from(code);
        return e == null ? "" : e.getName();
    }
}
