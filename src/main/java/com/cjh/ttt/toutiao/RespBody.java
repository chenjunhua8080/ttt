package com.cjh.ttt.toutiao;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RespBody {

    private int errcode;
    private String errmsg;

    private String sessionKey;
    private String openid;
    private String anonymousOpenid;

    private String access_token;
    private String expires_in;

}
