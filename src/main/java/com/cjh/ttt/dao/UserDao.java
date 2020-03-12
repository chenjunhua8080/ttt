package com.cjh.ttt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjh.ttt.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表(User)表数据库访问层
 *
 * @author cjh
 * @since 2020-02-27 15:16:43
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 根据openId查找
     *
     * @param openId openId
     * @return User
     */
    User selectByOpenId(@Param("openId") String openId);
}