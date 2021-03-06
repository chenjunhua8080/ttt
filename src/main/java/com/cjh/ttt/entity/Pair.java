package com.cjh.ttt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 配对表(Pair)实体类
 *
 * @author cjh
 * @since 2020-03-25 18:21:23
 */
@Data
@TableName("pair")
public class Pair implements Serializable {

    private static final long serialVersionUID = 274559096237355335L;
    
    @TableId
    private Integer id;
    /**
     * 添加渠道，字典[pail.channel]
     */
    private Integer channel;
    /**
     * 发送者
     */
    private Integer sender;
    /**
     * 接收者
     */
    private Integer recipient;
    /**
     * 解除者
     */
    private Integer relieve;
    /**
     * 内容
     */
    private String content;
    /**
     * 状态：0已送达1同意2拒绝3解除，字典[pail.status]
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