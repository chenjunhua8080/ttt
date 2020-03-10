package com.cjh.ttt.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * tokenDto
 *
 * @author cjh
 * @date 2020/2/28
 */
@NoArgsConstructor
@Data
public class DictDto {

    /**
     * 类型
     */
    private String type;
    /**
     * 键
     */
    private Integer dictKey;
    /**
     * 值
     */
    private String dictValue;
}
