package com.cjh.ttt.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 消息list dto
 *
 * @author cjh
 * @since 2020-03-25 18:21:22
 */
@Data
public class MessageDto implements Serializable {

    private Integer id;
    /**
     * 对方类型：1用户2群聊
     */
    private Integer targetType;

    /**
     * 对方id，现实用户id，可能发展成群聊~
     */
    private Integer targetId;

    /**
     * 对方头像
     */
    private String targetAvatar;
    /**
     * 对方昵称
     */
    private String targetNickname;

    /**
     * 上次消息时间
     */
    private Date lastTime;

    /**
     * 上次消息预览文本
     */
    private String lastText;

    /**
     * 未读统计
     */
    private Integer unrealCount;

}