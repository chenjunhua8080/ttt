package com.cjh.ttt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 配对列表标题枚举
 *
 * @author cjh
 * @date 2020/2/19
 */
@Getter
@AllArgsConstructor
public enum PairTypeEnum {

    /**
     * 配对列表类型枚举
     */
    PAIR_TYPE_1(1, "命中注定"),
    PAIR_TYPE_2(2, "专属推荐"),
    PAIR_TYPE_3(3, "离我最近"),
    ;

    private int code;
    private String name;

    public static PairTypeEnum from(Integer code) {
        if (code == null) {
            return null;
        }
        for (PairTypeEnum e : PairTypeEnum.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        PairTypeEnum e = from(code);
        return e == null ? "" : e.getName();
    }
}
