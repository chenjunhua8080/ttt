package com.cjh.ttt.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 消息内容表(MessageDetail)实体类
 *
 * @author cjh
 * @since 2020-03-27 17:15:33
 */
@Data
@TableName("message_detail")
public class MessageDetail implements Serializable {
    private static final long serialVersionUID = 151771246222168340L;
    
    @TableId
    private Integer id;
    /**
     * 消息id
     */
    private Integer messageId;
    /**
     * 对方消息id，[系统/解除关系]发送的消息为空
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
     * 状态：0未读1已读2已撤回，字典[message.status]
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 数据级别 0无效 1有效
     */
    @TableLogic
    private Integer dataLevel;

}