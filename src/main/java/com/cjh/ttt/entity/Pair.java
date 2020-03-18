package com.cjh.ttt.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * (Pair)实体类
 *
 * @author cjh
 * @since 2020-03-16 22:56:18
 */
@Data
@TableName("pair")
public class Pair implements Serializable {
    private static final long serialVersionUID = -20577142081902404L;
    
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
     * 内容
     */
    private String content;
    /**
     * 状态：1发送2同意3拒绝
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