package com.cjh.ttt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjh.ttt.entity.User;
import java.util.Date;
import java.util.List;
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
    User selectByOpenId(String openId);

    /**
     * 根据手机号查询
     */
    User selectByPhone(String phone);

    /**
     * 根据生日查询
     */
    List<User> selectByBirthday(Date birthday);

    /**
     * 根据性别、生日查询，10条
     */
    IPage<User> selectBySexAndBirthday(Page<User> page,
        @Param("sex") int sex,
        @Param("birthday") Date birthday,
        @Param("ids") List<Integer> ids);

    /**
     * 根据性别、生日查询，10条
     */
    IPage<User> selectBySexAndNearByBirthday(Page<User> page,
        @Param("sex") int sex,
        @Param("birthday") Date birthday,
        @Param("ids") List<Integer> ids);

    /**
     * 根据距离查询
     */
    IPage<User> selectByDistance(@Param("page") Page<User> page,
        @Param("lng") String lng,
        @Param("lat") String lat,
        @Param("ids") List<Integer> ids);
}