package com.cjh.ttt.request;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
    @NotBlank(message = "头像不能为空")
    private String avatar;
    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    @Length(max = 10, message = "昵称最大长度10")
    private String nickname;
    /**
     * 性别，字典[sex]
     */
    @Min(value = 1, message = "性别选择无效")
    @Max(value = 2, message = "性别选择无效")
    private Integer sex;
    /**
     * 生日
     */
    @Past(message = "生日设置不正确")
    private Date birthday;
    /**
     * 手机号
     */
    @Pattern(regexp = "1\\d{10}", message = "手机号不正确")
    private String phone;

}