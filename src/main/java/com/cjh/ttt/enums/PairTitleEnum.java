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
public enum PairTitleEnum {

    /**
     * 配对列表标题枚举
     */
    TITLE1(1, "命中注定"),
    TITLE2(2, "专属推荐"),
    TITLE3(3, "萍水相逢"),
    ;

    private int code;
    private String name;

    public static PairTitleEnum from(Integer code) {
        if (code == null) {
            return null;
        }
        for (PairTitleEnum e : PairTitleEnum.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        PairTitleEnum e = from(code);
        return e == null ? "" : e.getName();
    }
}
