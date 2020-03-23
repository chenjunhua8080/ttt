package com.cjh.ttt.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 消息表(Message)实体类
 *
 * @author cjh
 * @since 2020-03-23 11:33:37
 */
@Data
@TableName("message")
public class Message implements Serializable {
    private static final long serialVersionUID = 323348913997648462L;
    
    @TableId
    private Integer id;
    /**
     * 发送者
     */
    private Integer sender;
    /**
     * 接收者
     */
    private Integer recipient;
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
    private Integer dataLevel;

}