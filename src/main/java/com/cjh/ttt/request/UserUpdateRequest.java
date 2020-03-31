package com.cjh.ttt.request;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.Pattern;
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
     * 头像
     */
    @Pattern(regexp = "http://images\\.springeasy\\.cn/.*|http://1\\d{2}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}/ttt.*", message = "头像路径错误")
    private String avatar;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 手机号
     */
    @Pattern(regexp = "1\\d{10}", message = "手机号不正确")
    private String phone;

}