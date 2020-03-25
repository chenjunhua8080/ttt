package com.cjh.ttt.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * userId
 *
 * @author cjh
 * @date 2020/3/10
 */
@Data
public class UserIdRequest {

    @NotNull(message = "userId不能为空")
    public Integer userId;

}
