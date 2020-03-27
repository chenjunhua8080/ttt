package com.cjh.ttt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjh.ttt.dto.MessageDto;
import com.cjh.ttt.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 消息表(Message)表数据库访问层
 *
 * @author cjh
 * @since 2020-03-25 18:16:48
 */
@Mapper
public interface MessageDao extends BaseMapper<Message> {

    /**
     * 查询用户消息列表
     */
    IPage<MessageDto> getMessageList(@Param("page") Page<MessageDto> page, @Param("userId") Integer userId);

    /**
     * 查询会话，根据用户和对方
     */
    Message selectByUserAndTarget(@Param("userId") Integer userId, @Param("targetId") Integer targetId);
}