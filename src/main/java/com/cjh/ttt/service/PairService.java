package com.cjh.ttt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjh.ttt.dto.PairDto;
import com.cjh.ttt.entity.Pair;
import com.cjh.ttt.entity.User;
import com.cjh.ttt.request.PairingRequest;

/**
 * (Pair)表服务接口
 *
 * @author cjh
 * @since 2020-03-16 22:56:18
 */
public interface PairService extends IService<Pair> {


    /**
     * 获取配对列表
     * @return
     */
    PairDto getPairList(Page<User> page, Integer type);

    /**
     * 执行配对
     */
    void pairing(PairingRequest pairingRequest);

    /**
     * 配对坚持
     */
    int check(Integer id);
}