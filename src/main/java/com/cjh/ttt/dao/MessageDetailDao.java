package com.cjh.ttt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjh.ttt.entity.MessageDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息内容表(MessageDetail)表数据库访问层
 *
 * @author cjh
 * @since 2020-03-25 18:16:50
 */
@Mapper 
public interface MessageDetailDao extends BaseMapper<MessageDetail> {

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}