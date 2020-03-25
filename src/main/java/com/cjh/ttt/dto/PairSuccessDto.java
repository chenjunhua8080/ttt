package com.cjh.ttt.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author cjh
 * @date 2020/3/25
 */
@Data
@NoArgsConstructor
public class PairSuccessDto {

    /**
     * 发送者
     */
    private PairUserBean sender;
    /**
     * 接收者
     */
    private PairUserBean recipient;

    @NoArgsConstructor
    @Data
    public static class PairUserBean {

        private String nickname;
        private String avatar;
        private Integer sex;
    }
}
