package com.cjh.ttt.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 地址表(Address)实体类
 *
 * @author cjh
 * @since 2020-03-19 15:41:08
 */
@Data
@TableName("address")
public class Address implements Serializable {
    private static final long serialVersionUID = -57982087815704871L;
    
    @TableId
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 经度
     */
    private String lng;
    /**
     * 维度
     */
    private String lat;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String area;
    /**
     * 街道
     */
    private String street;
    /**
     * 详细地址
     */
    private String detail;
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
    private Integer dataLevel;

}