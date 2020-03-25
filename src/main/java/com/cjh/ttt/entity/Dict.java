package com.cjh.ttt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 字典表(Dict)实体类
 *
 * @author cjh
 * @since 2020-03-25 18:21:22
 */
@Data
@TableName("dict")
public class Dict implements Serializable {

    private static final long serialVersionUID = -44615194047459321L;
    
    @TableId
    private Integer id;
    /**
     * 类型
     */
    private String type;
    /**
     * 键
     */
    private Integer dictKey;
    /**
     * 值
     */
    private String dictValue;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 数据级别 0删除 1正常
     */
    @TableLogic
    private Integer dataLevel;

}