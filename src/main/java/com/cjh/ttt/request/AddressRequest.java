package com.cjh.ttt.request;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 地址表(Address)实体类
 *
 * @author cjh
 * @since 2020-03-19 15:41:08
 */
@Data
public class AddressRequest implements Serializable {

    private static final long serialVersionUID = -57982087815704871L;

    /**
     * 经度
     */
    @NotBlank(message = "lng 不能为空")
    private String lng;
    /**
     * 维度
     */
    @NotBlank(message = "lat 不能为空")
    private String lat;

}