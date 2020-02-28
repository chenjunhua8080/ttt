package com.cjh.ttt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjh.ttt.entity.Dict;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Dict)表数据库访问层
 *
 * @author cjh
 * @since 2020-02-27 15:16:41
 */
@Mapper 
public interface DictDao extends BaseMapper<Dict> {

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}