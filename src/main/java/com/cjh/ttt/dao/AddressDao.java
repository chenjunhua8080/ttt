package com.cjh.ttt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjh.ttt.entity.Address;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地址表(Address)表数据库访问层
 *
 * @author cjh
 * @since 2020-03-19 15:41:08
 */
@Mapper
public interface AddressDao extends BaseMapper<Address> {

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 查询用户地址
     */
    Address selectByUserId(Integer userId);
}