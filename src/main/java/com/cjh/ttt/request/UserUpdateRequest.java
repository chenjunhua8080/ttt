package com.cjh.ttt.request;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表(User)dto
 *
 * @author cjh
 * @since 2020-02-28 11:04:47
 */
@Data
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = -666069832584671L;

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

}