package com.cjh.ttt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjh.ttt.entity.Address;

/**
 * 地址表(Address)表服务接口
 *
 * @author cjh
 * @since 2020-03-19 15:41:08
 */
public interface AddressService extends IService<Address> {

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}