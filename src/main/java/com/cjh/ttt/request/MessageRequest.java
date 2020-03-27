package com.cjh.ttt.request;

import lombok.Data;

/**
 * 删除消息
 *
 * @author cjh
 * @date 2020/3/10
 */
@Data
public class MessageRequest {

    /**
     * 消息id
     */
    public Integer messageId;

    /**
     * 消息明细id
     */
    public Integer messageDetailId;

    /**
     * 内容
     */
    public String content;

}
