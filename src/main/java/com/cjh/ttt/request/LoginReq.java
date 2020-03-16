package com.cjh.ttt.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录参数
 *
 * @author cjh
 * @date 2020/3/10
 */
@Data
public class LoginReq {

    /**
     * 通过code换取openId
     * https://developer.toutiao.com/dev/cn/mini-app/develop/server/log-in/code2session
     */
    @NotBlank(message = "code 不能为空")
    public String code;

    public String avatar;
    public String nickname;
    public Integer sex;

}
