package com.cjh.ttt.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PairDto {

    private Integer pairType;
    private String pairTypeValue;
    private IPage<UserDto> pageData;

}
