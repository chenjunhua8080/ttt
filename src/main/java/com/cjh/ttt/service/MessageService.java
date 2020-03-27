package com.cjh.ttt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjh.ttt.dto.MessageDetailDto;
import com.cjh.ttt.dto.MessageDto;
import com.cjh.ttt.entity.Message;

/**
 * 消息表(Message)表服务接口
 *
 * @author cjh
 * @since 2020-03-25 18:16:48
 */
public interface MessageService extends IService<Message> {

    /**
     * 我的消息列表
     */
    IPage<MessageDto> getMessageList(Page<MessageDto> page);

    /**
     * 消息明细列表
     */
    IPage<MessageDetailDto> getDetailList(Page<MessageDetailDto> page, Integer messageId);

    /**
     * 删除会话
     */
    void delete(Integer messageId);

    /**
     * 删除消息
     */
    void deleteMessageDetail(Integer messageDetailId);

    /**
     * 撤回消息
     */
    void withdraw(Integer messageDetailId);

    /**
     * 发送消息
     */
    void send(Integer messageId, String content);
}