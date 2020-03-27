package com.cjh.ttt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjh.ttt.dto.MessageDetailDto;
import com.cjh.ttt.dto.MessageDto;
import com.cjh.ttt.entity.MessageDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 消息内容表(MessageDetail)表数据库访问层
 *
 * @author cjh
 * @since 2020-03-25 18:16:50
 */
@Mapper
public interface MessageDetailDao extends BaseMapper<MessageDetail> {

    /**
     * 查询会话的最后一条消息
     */
    MessageDetail selectLastMessage(Integer messageId);

    /**
     * 查询会话的消息列表
     */
    IPage<MessageDetailDto> getMessageDetailList(@Param("page") Page<MessageDetailDto> page,
        @Param("messageId") Integer messageId);

    /**
     * 统计未读数量
     */
    Integer countUnread(Integer messageId);
}