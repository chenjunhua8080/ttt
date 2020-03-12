package com.cjh.ttt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表(User)实体类
 *
 * @author cjh
 * @since 2020-02-28 11:04:47
 */
@Data
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = -66606963832584671L;

    @TableId
    private Integer id;
    /**
     * openId
     */
    private String openId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 性别，字典[sex]
     */
    private Integer sex;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 数据级别 0无效 1正常
     */
    @TableLogic
    private Integer dataLevel;

    @TableField(exist = false)
    private String token;

}