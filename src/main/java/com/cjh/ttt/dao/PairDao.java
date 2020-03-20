package com.cjh.ttt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cjh.ttt.entity.Pair;
import com.cjh.ttt.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * (Pair)表数据库访问层
 *
 * @author cjh
 * @since 2020-03-16 22:56:18
 */
@Mapper
public interface PairDao extends BaseMapper<Pair> {

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 统计匹配次数
     */
    int selectBySender(Integer sender);

    /**
     * 根据发送者,接收者查询
     */
    Pair selectBySenderAndRecipient(@Param("sender") Integer sender, @Param("recipient") Integer recipient);

    /**
     * 根据距离查询
     */
    IPage<User> selectByDistance(@Param("lng") String lng, @Param("lat") String lat);
}