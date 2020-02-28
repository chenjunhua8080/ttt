package com.cjh.ttt.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * (Message)实体类
 *
 * @author cjh
 * @since 2020-02-27 17:43:53
 */
@Data
@TableName("message")
public class Message implements Serializable {
    private static final long serialVersionUID = 426338729184131110L;
    
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
     * 消息类型，字典[message_type]
     */
    private Integer messageType;
    /**
     * 内容
     */
    private String content;
    /**
     * 已读
     */
    private Integer read;
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