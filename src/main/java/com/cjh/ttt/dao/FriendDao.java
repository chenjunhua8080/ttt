package com.cjh.ttt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjh.ttt.entity.Friend;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Friend)表数据库访问层
 *
 * @author cjh
 * @since 2020-02-27 15:16:43
 */
@Mapper 
public interface FriendDao extends BaseMapper<Friend> {

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}