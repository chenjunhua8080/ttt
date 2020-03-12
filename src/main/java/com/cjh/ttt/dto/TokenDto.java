package com.cjh.ttt.dto;

import java.util.Date;
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
public class TokenDto {

    private Integer userId;
    private String token;
    private Date expires;
}
