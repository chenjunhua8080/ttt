package com.cjh.ttt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 消息表(Message)实体类
 *
 * @author cjh
 * @since 2020-03-25 18:21:22
 */
@Data
@TableName("message")
public class Message implements Serializable {

    private static final long serialVersionUID = 846811084990701890L;

    @TableId
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 对方id，现实用户id，可能发展成群聊~
     */
    private Integer targetId;
    /**
     * 对方类型：1用户2群聊
     */
    private Integer targetType;
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