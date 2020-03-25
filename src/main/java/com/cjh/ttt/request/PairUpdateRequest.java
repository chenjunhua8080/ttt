package com.cjh.ttt.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * [同意/拒绝]配对
 *
 * @author cjh
 * @date 2020/3/10
 */
@Data
public class PairUpdateRequest {

    @NotNull(message = "配对用户不能为空")
    public Integer sender;
    @NotNull(message = "配对状态不能为空")
    @Min(value = 1, message = "参数错误")
    @Max(value = 2, message = "参数错误")
    public Integer status;

}
