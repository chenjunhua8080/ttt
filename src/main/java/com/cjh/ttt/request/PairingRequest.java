package com.cjh.ttt.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 执行配对
 *
 * @author cjh
 * @date 2020/3/10
 */
@Data
public class PairingRequest {

    @NotNull(message = "配对id不能为空")
    public Integer id;
    @NotBlank(message = "说点什么吧~")
    public String content;

}
