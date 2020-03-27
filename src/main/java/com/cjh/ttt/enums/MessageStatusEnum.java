package com.cjh.ttt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息状态枚举
 *
 * @author cjh
 * @date 2020/2/19
 */
@Getter
@AllArgsConstructor
public enum MessageStatusEnum {

    /**
     * 消息状态枚举
     */
    UNREAD(0, "未读"),
    READ(1, "已读"),
    WITHDRAW(2, "已撤回"),
    ;

    private int code;
    private String name;

    public static MessageStatusEnum from(Integer code) {
        if (code == null) {
            return null;
        }
        for (MessageStatusEnum e : MessageStatusEnum.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        MessageStatusEnum e = from(code);
        return e == null ? "" : e.getName();
    }
}
