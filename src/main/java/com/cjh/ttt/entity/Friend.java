package com.cjh.ttt.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * (Friend)实体类
 *
 * @author cjh
 * @since 2020-02-27 17:43:53
 */
@Data
@TableName("friend")
public class Friend implements Serializable {
    private static final long serialVersionUID = 457380007658537679L;
    
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
     * 打招呼内容
     */
    private String content;
    /**
     * 好友状态，字典[friend_status]
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