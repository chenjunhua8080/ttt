package com.cjh.ttt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjh.ttt.entity.Pair;
import java.util.List;
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
    int countPairSuccess(Integer userId);

    /**
     * 根据发送者,接收者查询
     */
    Pair selectBySenderAndRecipient(@Param("sender") Integer sender, @Param("recipient") Integer recipient);

    /**
     * 查询配对ids
     */
    List<Integer> selectPairIds(Integer userId);

    /**
     * 查询匹配状态
     */
    Integer selectStatus(@Param("sender") Integer sender, @Param("recipient") Integer recipient);
}