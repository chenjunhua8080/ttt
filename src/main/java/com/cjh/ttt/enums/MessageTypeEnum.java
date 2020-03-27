package com.cjh.ttt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型枚举
 *
 * @author cjh
 * @date 2020/2/19
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {

    /**
     * 消息类型枚举
     */
    SYSTEM(0, "系统消息"),
    TEXT(1, "文本消息"),
    IMAGE(2, "图片消息"),
    ;

    private int code;
    private String name;

    public static MessageTypeEnum from(Integer code) {
        if (code == null) {
            return null;
        }
        for (MessageTypeEnum e : MessageTypeEnum.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        MessageTypeEnum e = from(code);
        return e == null ? "" : e.getName();
    }
}
