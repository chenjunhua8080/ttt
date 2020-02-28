package com.cjh.ttt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjh.ttt.entity.Dict;

/**
 * (Dict)表服务接口
 *
 * @author cjh
 * @since 2020-02-27 15:16:41
 */
public interface DictService extends IService<Dict> {

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}