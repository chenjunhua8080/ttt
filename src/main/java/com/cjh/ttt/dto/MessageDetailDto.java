package com.cjh.ttt.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 消息内容表(MessageDetail)实体类
 *
 * @author cjh
 * @since 2020-03-25 18:21:23
 */
@Data
public class MessageDetailDto implements Serializable {


    private Integer id;
    /**
     * 对方消息id，为空是对方已解除了
     */
    private Integer targetDetailId;
    /**
     * 是否发送者：0否1是
     */
    private Integer isSender;
    /**
     * 消息类型，字典[message.type]
     */
    private Integer messageType;
    /**
     * 内容
     */
    private String content;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;

}