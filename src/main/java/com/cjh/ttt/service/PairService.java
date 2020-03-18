package com.cjh.ttt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjh.ttt.dto.PairDto;
import com.cjh.ttt.entity.Pair;
import com.cjh.ttt.request.PairingRequest;
import java.util.List;

/**
 * (Pair)表服务接口
 *
 * @author cjh
 * @since 2020-03-16 22:56:18
 */
public interface PairService extends IService<Pair> {


    /**
     * 获取配对列表
     */
    List<PairDto> getPairList();

    /**
     * 执行配对
     */
    void pairing(PairingRequest pairingRequest);

    /**
     * 配对坚持
     */
    int check(Integer id);
}