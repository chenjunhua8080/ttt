package com.cjh.ttt.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PairDto {

    private Integer pairType;
    private String pairTypeValue;
    private List<UserBean> userList;

    @NoArgsConstructor
    @Data
    public static class UserBean {

        private Integer id;
        private String avatar;
        private String nickname;
    }
}
